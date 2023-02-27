package ecom.market.limit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ecom.market.limit.services.ItemsService;

@RestController
public class ItemsController {

	@Autowired
	ItemsService itemsService;
	
	@GetMapping("/getItem")
	public String name() {
		itemsService.getItem();
		return "{}";
	}

}
