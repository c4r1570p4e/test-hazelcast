package cl.hazelcast.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hazelcast.core.IMap;

@Controller
public class CacheController {

	@Resource(name = "map1")
	private IMap<String, String> map1;

	@Resource(name = "map2")
	private IMap<String, String> map2;

	@Resource(name = "map3")
	private IMap<String, String> map3;

	@RequestMapping("/cache")
	public String cache(String cacheName, String id, String val, Model model) {

		IMap<String, String> map = null;

		model.addAttribute("cacheNames", cacheNames);

		if (!(cacheName == null || cacheName.isEmpty())) {

			switch (cacheName) {
			case "map1":
				map = map1;
				break;
			case "map2":
				map = map2;
				break;
			case "map3":
				map = map3;
				break;
			default:
				map = null;
				break;
			}

			model.addAttribute("cacheName", cacheName);
			model.addAttribute("cache", map);

			if (id != null && !id.isEmpty()) {
				// modif de l'entrée
				if (val != null && !val.isEmpty()) {
					map.put(id, val);
				} else {
					// suppression
					map.remove(id);
				}
			}
		}

		return "cache";
	}

	private static List<String> cacheNames = new ArrayList<>();

	static {
		cacheNames.add("");
		cacheNames.add("map1");
		cacheNames.add("map2");
		cacheNames.add("map3");

	}

}
