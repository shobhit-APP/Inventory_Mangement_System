package com.example.StockStick.Service;

import com.example.StockStick.AllException.CustomException;
import com.example.StockStick.Model.Items;
import com.example.StockStick.Repositery.itemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ItemsService {
    @Autowired
    private itemRepo repo;
   @Transactional
    public void SaveProduct(Items items) {
        repo.save(items);
    }
    @Transactional
    public List<Items> getSaveProduct(Long userId) {
        return repo.findByUserDetailsId(userId);
    }
    @Transactional
    public Items getSpecificSaveProduct(int itemId) {
        return repo.findById(itemId).orElseThrow(() -> new CustomException("Sorry , User There is No Product With this Item Id Please Try Again"));
    }
    @Transactional
    public void deleteItemById(int itemId) {
        repo.deleteById(itemId);
    }
    @Transactional
    public void deleteAllItemById(Long userId) {
        repo.deleteByUserDetailsId(userId);
    }
//    @Transactional
//    public double getTotalQuantityOfAnyItem(List<Items> itemsList, String emailAddress, String category) {
//        return itemsList.stream()
//                .filter(items -> Objects.equals(emailAddress, items.getUserDetails().getEmailAddress()))
//                .mapToDouble(Items::getItem_price)
//                .sum();
//        // Corrected to map to item price for summing
//        // Sum the prices
//    }
    @Transactional
     //this general method
    public double getTotalQuantityOfAnyItem(List<Items> itemsList, Long userId) {
        double TotalQuantity=  0;
       for(Items Item:itemsList)
       {
           if(Objects.equals(userId, Item.getUserDetails().getId())){
               TotalQuantity +=Item.getItem_price();
           }
       }
       return TotalQuantity;
    }
    @Transactional
    //This Advance Method
    public double getTotalQuantityOfAnyItem(List<Items> itemsList, Long userId, String categories) {
        return itemsList.stream()
                .filter(items -> Objects.equals(userId,items.getUserDetails().getId()))
                .map(Items::getItem_categories)
                .distinct()
                .count();

    }

    public void SaveAll(List<Items> items) {
       repo.saveAll(items);
    }
}
