package ecom.market.limit.services;

import org.springframework.stereotype.Service;

import ecom.market.limit.annotations.RequestLimit;

@Service
public class ItemsService {

    @RequestLimit
    public String getItem() {
        return "item482";
    }
    
}
