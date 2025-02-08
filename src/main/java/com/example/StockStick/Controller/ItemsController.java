package com.example.StockStick.Controller;

import com.example.StockStick.AllException.CustomException;
import com.example.StockStick.Model.Items;
import com.example.StockStick.Model.UserDetails;
import com.example.StockStick.Model.UserPrinciple;
import com.example.StockStick.Repositery.UserRepo;
import com.example.StockStick.Service.ItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
public class ItemsController {
    public static final Logger log = LoggerFactory.getLogger(ItemsController.class);
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private UserRepo userRepo;
    @GetMapping("/Addnew")
    public String AddProduct()
    {
        return "Addnew";
    }
    @PatchMapping("/updateQuantity/{itemId}")
    public ResponseEntity<?> UpdateTheQuantityOfSpecific(@PathVariable("itemId") int itemId, @RequestBody Map<String ,Integer> payload) {
        int quantity = payload.get("quantity");
        Items Item = itemsService.getSpecificSaveProduct(itemId);
        Item.setQuantity(quantity);
        itemsService.SaveProduct(Item);
        return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK); // Return a JSON response
    }

    @PostMapping("/Addnew")
    public ResponseEntity<?> AddNewProduct(@ModelAttribute Items items , Model model ,@AuthenticationPrincipal UserPrinciple userPrinciple)
    {

        if(items.NullChecker(items))
        {
          return ResponseEntity.ok().body("{\"message\": \"Items Not Should be null \"}");
        }
           Double itemPrice=items.getItem_price();
          if( itemPrice == null || Double.isNaN(itemPrice) || itemPrice <= 0)
          {
              return ResponseEntity.ok().body("{\"message\": \"Please Enter valid Price\"}");
          }
          try
          {
              String emailAddress = userPrinciple.getUsername();
              Long userId=userPrinciple.getUserId();
              if(userId < 0)
              {
                  log.error("userid is empty");
                  throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");
              }
              UserDetails user= userRepo.findByEmailAddress(emailAddress);
              if(user==null)
              {
                  log.error("No user found with email address: {}", emailAddress);
                  throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");
              }
              items.setUserDetails(user);
          }
          catch (CustomException e)
          {
              log.error("Error in setting user details: {}", e.getMessage());
              throw new CustomException("Please check your internet connectivity. If the connection is correct, the issue might be on our side. Please wait for 10 minutes or try again later!!!");
          }
          catch (Exception e)
          {
              log.error("An unexpected error occurred: {}", e.getMessage());
              throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");

          }
          System.out.println(items.getItem_name());
          LocalDate localDate=LocalDate.now();
          items.setItem_lastUpdate(localDate);
          itemsService.SaveProduct(items);
          return ResponseEntity.ok().body("{\"message\": \"Item Added Successfully To The Inventory\"}");
    }
    @GetMapping("/UpdateItem/{item_id}")
    public String showUpdateProductPage(@PathVariable("item_id") int item_id, Model model)
    {

        try {
            // Fetching the item from the database Jo I'd ayi hai mera pass Inventory se
            Items items = itemsService.getSpecificSaveProduct(item_id);
            if (items.NullChecker(items)) {
                log.error("Item details are missing or invalid for item_id: {}", item_id);
                throw  new CustomException("An internal error occurred. Please try again later or contact us for assistance.!!!");
            }
            //Sending Fetching data to updateItem page
            model.addAttribute("items", items);
        }
        catch (CustomException e)
        {
            log.error("Error in fetching item details: {}", e.getMessage());
            throw new CustomException("Please check your internet connectivity. If the connection is correct, the issue might be on our side. Please wait for 10 minutes or try again later!!!");
        }
        catch (Exception e)
        {
            log.error("Unexpected error occurred: {}", e.getMessage());
            throw  new CustomException("An internal error occurred. Please try again later or contact us for assistance.!!!");
        }
        return "UpdateItem";
    }
    @PutMapping("/UpdateItem/{item_id}")
    public ResponseEntity<?> UpdateNewProduct(@PathVariable("item_id") int item_id ,@ModelAttribute Items items)
    {
        //Real Work tho is method mai Hai
        //This method is used tho update The Item in The Inventory  and Also In The DataBase
        try {
            Items existingItem=itemsService.getSpecificSaveProduct(item_id);

            if (existingItem.NullChecker(existingItem)) {
                log.error("Item details are missing or invalid for The item_id: {}", item_id);
                throw  new CustomException("An internal error occurred So,Items Could  Not Update At This Time . Please try again later or contact us for assistance.!!!");
            }
            //Sending Fetching data to updateItem page
            existingItem.setItem_name(items.getItem_name());
            existingItem.setItem_categories(items.getItem_categories());
            existingItem.setItem_description(items.getItem_description());
            existingItem.setItem_price(items.getItem_price());
            existingItem.setQuantity(items.getQuantity());
            existingItem.setStatus(items.getStatus());
            existingItem.setItem_lastUpdate(LocalDate.now());
            itemsService.SaveProduct(existingItem);
        }
        catch (CustomException e)
        {
            log.error("Error in fetching The item details: {}", e.getMessage());
            throw e; // Re-throwing the custom exception to handle it appropriately
        }
        catch (Exception e)
        {
            log.error("An unexpected Error occurred: {}", e.getMessage());
            throw  new CustomException("An internal error occurred. Please try again later or contact us for assistance.!!!");
        }

        return ResponseEntity.ok().body("{\"message\": \"Item  Successfully Updated In The Inventory\"}");
    }
    @GetMapping("/Inventory")
    public String Inventory(Model model, @AuthenticationPrincipal UserPrinciple userPrinciple)
    {
        String emailAddress;
        Items items = new Items();
        try {
             emailAddress = userPrinciple.getUsername();
             Long userId= userPrinciple.getUserId();
            if (userId < 0) {
                log.error("User Id  is empty");
                throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");
            }
        }
        catch (CustomException e)
        {
            log.error("One Of the Exception Is Occur from These Three : {}", userPrinciple);
            throw new CustomException("An internal error occurred. We could not display your item. Please try again later or contact us for assistance!!");
        }
        catch (Exception e)
        {
            log.error("An unexpected Error Occurred: {}", e.getMessage());
            throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");
        }
             UserDetails userDetails = userRepo.findByEmailAddress(emailAddress);
             List<Items> ItemsList = itemsService.getSaveProduct(userPrinciple.getUserId());
             int TotalItem= ItemsList.size();
             String Categories=items.getItem_categories();
             double TotalPrice=itemsService.getTotalQuantityOfAnyItem(ItemsList , userPrinciple.getUserId());
             double TotalCategories=itemsService.getTotalQuantityOfAnyItem(ItemsList , userPrinciple.getUserId(), Categories);
        if (ItemsList.isEmpty() && LocalDate.now().equals(userPrinciple.getRDate())) {
            model.addAttribute("greet" ,"Welcome");
            model.addAttribute("Status", "Your inventory is currently empty. Please add some stock and personalize your inventory if you wish!");
        }
        else if(ItemsList.isEmpty() && !LocalDate.now().equals(userPrinciple.getRDate())){
            model.addAttribute("greet" ,"Welcome Back");
            model.addAttribute("status2","We miss You");
            model.addAttribute("Status", "Your inventory is currently empty. Please add some stock and personalize your inventory if you wish!");
        }
        else {
            model.addAttribute("greet" ,"Welcome Back");
            model.addAttribute("status2","We miss You");
        }
        model.addAttribute("TotalItem", TotalItem);
        model.addAttribute("TotalPrice", TotalPrice);
        model.addAttribute("TotalCategories", TotalCategories);
        model.addAttribute("ItemsList", ItemsList);
        model.addAttribute("emailAddress", userPrinciple.getUsername());
        model.addAttribute("firstNameLastName", userDetails.getFirstName().substring(0, 1).toUpperCase() + userDetails.getFirstName().substring(1) + " " + userDetails.getLastName().substring(0, 1).toUpperCase() + userDetails.getLastName().substring(1));
        model.addAttribute("ContactNo", userDetails.getContactNo());
        model.addAttribute("country", userDetails.getCountry());
        model.addAttribute("state", userDetails.getState());
        model.addAttribute("UserType", userDetails.getUsertype());
        return "Inventory";
    }
    @DeleteMapping("/DeleteItem/{item_id}")
    public ResponseEntity<String> DeleteTheItem(@PathVariable("item_id") int item_id ,@AuthenticationPrincipal UserPrinciple user)
    {
        Items existingItem=itemsService.getSpecificSaveProduct(item_id);
        if(!existingItem.NullChecker(existingItem))
        {
            itemsService.deleteItemById(item_id);
            return new  ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Failed To Delete",HttpStatus.NOT_FOUND);

    }
}
