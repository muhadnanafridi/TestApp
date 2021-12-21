package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsBundle implements Serializable {

    public ArrayList<Data> data = new ArrayList<>();

    public Meta meta = new Meta();

    public static class Data  implements Serializable {

        int subcategory_id,quantity,totalitems,bulk_quantity,item_count,inventory;
        String id,name, description,thumbnail,packaging_quantity_weight,category,packaging_quantity;
        Float maximum_selling_price,valucart_price,total_percentage,total_products_price,total_price,percentage_discount;
        int addToByob = 0;
        ArrayList<String> images = new ArrayList<>();
        PackagingQuantity packaging_quantity_unit = new PackagingQuantity();
        String productType="";
        Boolean is_bulk,is_wishList=false;

        public void setCategory(String category) {
            this.category = category;
        }

        public static class Category  implements Serializable{
            String id,name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public int getItem_count() {
            return item_count;
        }

        public void setItem_count(int item_count) {
            this.item_count = item_count;
        }

        public String getCategory() {
            return category;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getThumbnail() {
            return thumbnail==null?"":thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

        public PackagingQuantity getPackaging_quantity_unit() {
            return packaging_quantity_unit;
        }

        public void setPackaging_quantity_unit(PackagingQuantity packaging_quantity_unit) {
            this.packaging_quantity_unit = packaging_quantity_unit;
        }

        public static class PackagingQuantity  implements Serializable {

            String id,name,symbol="";

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSymbol() {
                return symbol==null ? "" : symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSubcategory_id() {
            return subcategory_id;
        }

        public void setSubcategory_id(int subcategory_id) {
            this.subcategory_id = subcategory_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPackaging_quantity() {
            return packaging_quantity;
        }

        public void setPackaging_quantity(String packaging_quantity) {
            this.packaging_quantity = packaging_quantity;
        }

        public Float getMaximum_selling_price() {
            return maximum_selling_price;
        }

        public void setMaximum_selling_price(Float maximum_selling_price) {
            this.maximum_selling_price = maximum_selling_price;
        }

        public Float getValucart_price() {
            return valucart_price;
        }

        public void setValucart_price(Float valucart_price) {
            this.valucart_price = valucart_price;
        }

        public int getAddToByob() {
            return addToByob;
        }

        public void setAddToByob(int addToByob) {
            this.addToByob = addToByob;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getPackaging_quantity_weight() {
            return packaging_quantity_weight;
        }

        public void setPackaging_quantity_weight(String packaging_quantity_weight) {
            this.packaging_quantity_weight = packaging_quantity_weight;
        }

        public Float getTotal_percentage() {
            return total_percentage;
        }

        public void setTotal_percentage(Float total_percentage) {
            this.total_percentage = total_percentage;
        }

        public Float getTotal_products_price() {
            return total_products_price;
        }

        public void setTotal_products_price(Float total_products_price) {
            this.total_products_price = total_products_price;
        }

        public Float getTotal_price() {
            return total_price;
        }

        public void setTotal_price(Float total_price) {
            this.total_price = total_price;
        }

        public int getTotalitems() {
            return totalitems;
        }

        public void setTotalitems(int totalitems) {
            this.totalitems = totalitems;
        }

        public int getBulk_quantity() {
            return bulk_quantity;
        }

        public void setBulk_quantity(int bulk_quantity) {
            this.bulk_quantity = bulk_quantity;
        }

        public Boolean getIs_bulk() {
            return is_bulk;
        }

        public void setIs_bulk(Boolean is_bulk) {
            this.is_bulk = is_bulk;
        }

        public Float getPercentage_discount() {
            return percentage_discount;
        }

        public void setPercentage_discount(Float percentage_discount) {
            this.percentage_discount = percentage_discount;
        }

        public Boolean getIs_wishList() {
            return is_wishList;
        }

        public void setIs_wishList(Boolean is_wishList) {
            this.is_wishList = is_wishList;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public static class Meta  implements Serializable{
        int current_page,from,last_page,per_page,to,total;
        String path;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
