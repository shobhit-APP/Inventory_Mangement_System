package com.example.StockStick.Model;

import com.example.StockStick.AllException.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Items")
public class Items {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       @Column(name = "item_id")
       private int item_id;
       @Column(name="item_name")
       private String item_name;
       @Column(name="item_description" , columnDefinition = "TEXT")
       private String item_description;
       @Column(name="status")
       private String status;
       @Column(name="quantity")
       private int quantity;
       @Column(name="item_categories")
       private String item_categories;
       @Column(name="item_lastUpdate")
       private LocalDate item_lastUpdate;
       @Column(name = "item_price")
       private Double item_price;
       @ManyToOne
       @JoinColumn(name = "userId", referencedColumnName = "Id", nullable = false)
       private UserDetails userDetails;
       //
//       private static final Logger log = LoggerFactory.getLogger(Items.class);
       public boolean NullChecker(Object obj)
       {
              if(obj==null)
              {
                     return true;
              }
              for (Field field:obj.getClass().getDeclaredFields())
              {
                     try {
                            if (field.get(obj) != null) {

                                   return false;
                            }
                     }
                     catch (IllegalAccessException e)
                     {
//                            log.error("Error accessing field: {}", field.getName(), e);
                            throw new CustomException("Please check your internet connectivity. If the connection is correct, the issue might be on our side. Please wait for 10 minutes or try again later!!!");
                     }
              }
              return true;
       }
}
