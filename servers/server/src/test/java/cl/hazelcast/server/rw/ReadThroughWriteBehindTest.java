package cl.hazelcast.server.rw;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/jdbcTestContext.xml" })
public class ReadThroughWriteBehindTest {

	private static final String KEY1 = "KEY1";
	private static final String VALUE1 = "VALUE1";
	private static final String KEY2 = "KEY2";
	private static final String VALUE2 = "VALUE2";
	private static final String KEY3 = "KEY3";
	private static final String VALUE3 = "VALUE3";
	private static final String KEY4 = "KEY4";
	private static final String VALUE4 = "VALUE4";
	private static final String KEY5 = "KEY5";
	private static final String VALUE5 = "VALUE5";

	@Autowired
	private ReadThroughWriteBehind rtwb;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void clean() {
		jdbcTemplate.execute("delete from TABLETEST;");
		jdbcTemplate.execute("INSERT INTO TABLETEST VALUES('" + KEY1 + "', '" + VALUE1 + "');");
		jdbcTemplate.execute("INSERT INTO TABLETEST VALUES('" + KEY2 + "', '" + VALUE2 + "');");
		jdbcTemplate.execute("INSERT INTO TABLETEST VALUES('" + KEY3 + "', '" + VALUE3 + "');");
		jdbcTemplate.execute("INSERT INTO TABLETEST VALUES('" + KEY4 + "', '" + VALUE4 + "');");
	}

	@Test
	public void storeAllTest() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY5, VALUE5);
		map.put(KEY4, VALUE4 + VALUE5);

		rtwb.storeAll(map);
		assertThat(jdbcTemplate.queryForObject("select VAL from TABLETEST where ID = '" + KEY5 + "';", String.class))
				.isEqualTo(VALUE5);
		assertThat(jdbcTemplate.queryForObject("select VAL from TABLETEST where ID = '" + KEY4 + "';", String.class))
				.isEqualTo(VALUE4 + VALUE5);

	}

	@Test
	public void storeTest() {

		rtwb.store(KEY5, VALUE5);
		rtwb.store(KEY4, VALUE4 + VALUE5);
		assertThat(jdbcTemplate.queryForObject("select VAL from TABLETEST where ID = '" + KEY5 + "';", String.class))
				.isEqualTo(VALUE5);
		assertThat(jdbcTemplate.queryForObject("select VAL from TABLETEST where ID = '" + KEY4 + "';", String.class))
				.isEqualTo(VALUE4 + VALUE5);
	}

	@Test
	public void loadAllKeysTest() {
		assertThat(rtwb.loadAllKeys()).containsOnly(KEY1, KEY2, KEY3, KEY4);
	}

	@Test
	public void loadAllTest() {

		Collection<String> keys = new HashSet<String>();
		keys.add(KEY1);
		keys.add(KEY3);
		keys.add(KEY5);

		assertThat(rtwb.loadAll(keys)).includes(entry(KEY1, VALUE1), entry(KEY3, VALUE3));
		assertThat(rtwb.loadAll(keys)).excludes(entry(KEY2, VALUE2), entry(KEY4, VALUE4), entry(KEY5, VALUE5));
	}

	@Test
	public void loadTest() {

		assertThat(rtwb.load(KEY1)).isEqualTo(VALUE1);
		assertThat(rtwb.load(KEY5)).isNull();

	}

	@Test
	public void deleteAllTest() {

		Set<String> keys = new HashSet<String>();
		keys.add(KEY1);
		keys.add(KEY3);

		rtwb.deleteAll(keys);

		assertThat(jdbcTemplate.queryForList("Select ID  from TABLETEST", String.class)).containsOnly(KEY2, KEY4);
	}

	@Test
	public void deleteTest() {

		rtwb.delete(KEY3);

		assertThat(
				jdbcTemplate.queryForObject("select count(1) from TABLETEST where ID = '" + KEY3 + "';", Integer.class))
				.isEqualTo(0);

	}

}
