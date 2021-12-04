package com.liasplf.ncumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liasplf.ncumall.dao.ItemCategoryDao;
import com.liasplf.ncumall.dao.ItemDao;
import com.liasplf.ncumall.po.Item;
import com.liasplf.ncumall.po.ItemCategory;
import com.liasplf.ncumall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("itemServiceImpl")
public class ItemServiceImpl extends ServiceImpl<ItemDao, Item> implements ItemService {
    @Autowired
    private ItemCategoryDao itemCategoryDao;

    @Override
    public List<Item> getAll(Integer status, Integer id) {
        List<Item> list;
        if (status==0){
            list=this.getBaseMapper().selectList(new QueryWrapper<Item>().eq("isDelete",0));
            System.out.println(list);
        }else{
            list=this.getBaseMapper().selectList(new QueryWrapper<Item>().eq("manage_id",id).eq("isDelete",0));
        }

        List<ItemCategory> itemCategories = itemCategoryDao.selectList(null);
        Map<Integer,ItemCategory> map = new HashMap<Integer,ItemCategory>();
        for (ItemCategory itemCategory : itemCategories) {
            map.put(itemCategory.getId(),itemCategory);
        }
        for (Item item : list) {
            Integer id1 = item.getCategoryIdOne();
            Integer id2 = item.getCategoryIdTwo();

            item.setYiji(map.get(id1));
            item.setErji(map.get(id2));
        }
        return list;
    }

    @Override
    public List<Item> searchItem(String itemName, Integer status, Integer id) {

        List<Item> list = getAll(status,id);
        List<Item> ans = new ArrayList<Item>();
        for (Item item : list) {
            if(item.getName().contains(itemName))ans.add(item);
        }
        return ans;
    }
}
