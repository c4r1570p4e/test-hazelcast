package cl.hazelcast.server.map.interceptor;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

import com.hazelcast.map.MapInterceptor;

@Slf4j
public class LogMapInterceptor implements MapInterceptor, Serializable {

	private String mapName;

	public LogMapInterceptor(String mapName) {
		this.mapName = mapName;
	}

	@Override
	public Object interceptGet(Object value) {
		log.debug("get : {} : {} ", mapName, value);
		return null;
	}

	@Override
	public void afterGet(Object value) {
	}

	@Override
	public Object interceptPut(Object oldValue, Object newValue) {
		log.debug("put : {} : {} ", mapName, newValue);
		return null;
	}

	@Override
	public void afterPut(Object value) {
	}

	@Override
	public Object interceptRemove(Object removedValue) {
		log.debug("remove : {} : {} ", mapName, removedValue);
		return null;
	}

	@Override
	public void afterRemove(Object value) {
	}

}
