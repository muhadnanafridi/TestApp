package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsDetails implements Serializable {

    public Data data = new Data();

    public static class Data  implements Serializable {

        int subcategory_id,bulk_quantity,inventory;
        String id,name, description,packaging_quantity,thumbnail;
        Float maximum_selling_price,valucart_price;
        int addToByob = 0;
        ArrayList<String> images = new ArrayList<>();
        PackagingQuantity packaging_quantity_unit;
        Boolean is_bulk;
        Category  category = new Category();
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
        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
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

            String id,name,symbol;

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

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
