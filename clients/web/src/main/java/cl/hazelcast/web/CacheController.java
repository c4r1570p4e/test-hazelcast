package cl.hazelcast.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CacheController {

	@RequestMapping("/cache")
	public String helloWorld(String cacheName, String id, String val, Model model) {
		
		List<String> cacheNames = new ArrayList<>();
		cacheNames.add("");
		cacheNames.add("map1");
		cacheNames.add("map2");
		cacheNames.add("map3");
		cacheNames.add("map4");
		cacheNames.add("map5");
		cacheNames.add("map6");

		model.addAttribute("cacheNames",cacheNames);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("KEY1", "VALUE1");
		map.put("KEY2", "VALUE2");		
		
		if(!(cacheName == null || cacheName.isEmpty())) {
			
			model.addAttribute("cacheName",cacheName);
			model.addAttribute("cache", map);
			
			if(id != null && !id.isEmpty()) {
				//modif de l'entrée
				if(val != null && !val.isEmpty()) {
					map.put(id, val);
				}
				else {
					//suppression
					map.remove(id);
				}
			}
		}
		
		return "cache";
	}
}
