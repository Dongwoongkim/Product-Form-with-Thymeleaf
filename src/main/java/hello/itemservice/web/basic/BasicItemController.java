package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model)
    {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";
    }

    /*
    테스트용 데이터 추가
     */

    @PostConstruct
    public void init()
    {
        itemRepository.save(new Item("ItemA",10000,10));
        itemRepository.save(new Item("ItemB",20000,20));
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model)
    {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm()
    {
        return "basic/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(@RequestParam String itemName,
//                            @RequestParam int price,
//                            @RequestParam Integer quantity,
//                            Model model)
//    {
//        Item item = new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);
//
//        itemRepository.save(item);
//
//        model.addAttribute("item",item);
//
//        return "basic/item";
//    }


//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute("item") Item item, Model model)
//    {
//        log.info("item = {}",item);
//        log.info("itemName = {} , itemPrice = {} , itemQuantity = {} ",
//                item.getItemName(),item.getPrice(),item.getQuantity());
//        itemRepository.save(item);
//
//        return "basic/item";
//    }

    /*
    @ModelAttribute의 이름을 생략하면 모델에 저장될 때 클래스명을 소문자로 변경해서 사용합니다.

     */
//    @PostMapping("/add")
//    public String addItemV3(@ModelAttribute Item item, Model model)
//    {
//        log.info("item = {}",item);
//        log.info("itemName = {} , itemPrice = {} , itemQuantity = {} ",
//                item.getItemName(),item.getPrice(),item.getQuantity());
//
//        itemRepository.save(item);
//        return "basic/item";
//    }

//    /*
//    @ModelAttribute 자체도 생략 가능.
//    객체의 경우 자동으로 ModelAttribute 적용
//    단순 타입의 경우 @RequestParam 적용
//     */
//    @PostMapping("/add")
//    public String addItemV4(Item item)
//    {
//        itemRepository.save(item);
//        return "redirect:/basic/items/" + item.getId();
//    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes, Model model)
    {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/clear")
    public String clearList()
    {
        itemRepository.clearStore();
        return "basic/items";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model)
    {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item)
    {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


}