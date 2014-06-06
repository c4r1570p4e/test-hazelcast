package cl.hazelcast.server.rw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapLoaderLifecycleSupport;
import com.hazelcast.core.MapStore;
import com.hazelcast.spring.context.SpringAware;

@Slf4j
@SpringAware
@Transactional
public class ReadThroughWriteBehind implements MapLoader<String, String>, MapStore<String, String>,
		MapLoaderLifecycleSupport {

	private String mapName;

	private String tableName;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcOperations namedParameterJdbcOperations;

	public ReadThroughWriteBehind(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
		log.debug("init " + mapName);
		this.mapName = mapName;
	}

	@Override
	public void destroy() {
		log.debug("destroy " + this.mapName);
	}

	@Override
	public String load(String key) {
		log.debug("load {}", key);

		String sql = "Select VAL from " + tableName + " where ID = ?";

		try {
			return jdbcTemplate.queryForObject(sql, String.class, key);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	private static class StringArrayRowMapper implements RowMapper<String[]> {

		@Override
		public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
			String[] retour = new String[2];

			retour[0] = rs.getString("ID");
			retour[1] = rs.getString("VAL");

			return retour;
		}

	}

	@Override
	public Map<String, String> loadAll(Collection<String> keys) {
		log.debug("loadAll");

		String sql = "Select ID , VAL from " + tableName + " where ID in ( :ids )";

		Map<String, Collection<String>> params = new HashMap<String, Collection<String>>();
		params.put("ids", keys);

		List<String[]> sqlResult = namedParameterJdbcOperations.query(sql, params, new StringArrayRowMapper());

		Map<String, String> map = new HashMap<String, String>();

		for (String[] strings : sqlResult) {
			map.put(strings[0], strings[1]);
		}

		return map;
	}

	@Override
	public Set<String> loadAllKeys() {
		log.debug("loadAllKeys");

		String sql = "Select ID from " + tableName;

		return new HashSet<String>(jdbcTemplate.queryForList(sql, String.class));

	}

	@Override
	public void store(String key, String value) {
		log.debug("store {} {}", key, value);

		String sql = "MERGE INTO " + tableName + " KEY(ID) VALUES(?, ?)";

		jdbcTemplate.update(sql, key, value);

	}

	private List<Object[]> toBatchParam(Map<String, String> map) {

		List<Object[]> retour = new ArrayList<Object[]>();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			Object[] tab = new Object[2];
			tab[0] = entry.getKey();
			tab[1] = entry.getValue();
			retour.add(tab);
		}
		return retour;
	}

	@Override
	public void storeAll(Map<String, String> map) {
		log.debug("storeAll");

		String sql = "MERGE INTO " + tableName + " KEY(ID) VALUES(?, ?)";
		jdbcTemplate.batchUpdate(sql, toBatchParam(map));
	}

	@Override
	public void delete(String key) {
		log.debug("delete {}", key);

		String sql = "DELETE " + tableName + " WHERE ID = ?";

		jdbcTemplate.update(sql, key);

	}

	private List<Object[]> toBatchParam(Collection<String> keys) {

		List<Object[]> retour = new ArrayList<Object[]>();

		for (String key : keys) {
			Object[] tab = new Object[1];
			tab[0] = key;
			retour.add(tab);

		}

		return retour;
	}

	@Override
	public void deleteAll(Collection<String> keys) {
		log.debug("deleteAll");

		String sql = "delete " + tableName + " where ID = ?";

		jdbcTemplate.batchUpdate(sql, toBatchParam(keys));

	}

}
