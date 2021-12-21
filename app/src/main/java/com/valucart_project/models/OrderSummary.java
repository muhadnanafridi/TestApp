package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSummary  implements Serializable {

    int status;
    public Data data = new Data();

    public static class Data implements Serializable {

        public ArrayList<Products> products = new ArrayList<>();
        public ArrayList<Products> customer_bundles = new ArrayList<>();
        public ArrayList<Products> bundles = new ArrayList<>();
        String total_price,delivery_date,delivery_time,status_string;
        public String delivery_address = new String();
        Float valucart_price,delivery_charge;
        public static class Products implements Serializable {
            int quantity;
            ProductsData item = new ProductsData();

            public static class ProductsData implements Serializable {
                String id,sku,name,description,thumbnail,packaging_quantity;
                Float maximum_selling_price,valucart_price,price;
                ArrayList<String> images = new ArrayList<>();
                PackagingQuantity packaging_quantity_unit = new PackagingQuantity();
                int addToByob = 0;

                public int getAddToByob() {
                    return addToByob;
                }

                public void setAddToByob(int addToByob) {
                    this.addToByob = addToByob;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getSku() {
                    return sku;
                }

                public void setSku(String sku) {
                    this.sku = sku;
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

                public String getThumbnail() {
                    return thumbnail;
                }

                public void setThumbnail(String thumbnail) {
                    this.thumbnail = thumbnail;
                }

                public Float getPrice() {
                    return price;
                }

                public void setPrice(Float price) {
                    this.price = price;
                }
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

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public ProductsData getItem() {
                return item;
            }

            public void setItem(ProductsData item) {
                this.item = item;
            }

        }

        public ArrayList<Products> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<Products> products) {
            this.products = products;
        }

        public ArrayList<Products> getCustomer_bundles() {
            return customer_bundles;
        }

        public void setCustomer_bundles(ArrayList<Products> customer_bundles) {
            this.customer_bundles = customer_bundles;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getDelivery_date() {
            return delivery_date;
        }

        public void setDelivery_date(String delivery_date) {
            this.delivery_date = delivery_date;
        }

        public String getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(String delivery_time) {
            this.delivery_time = delivery_time;
        }

        public String getDelivery_address() {
            return delivery_address;
        }

        public void setDelivery_address(String delivery_address) {
            this.delivery_address = delivery_address;
        }

        public ArrayList<Products> getBundles() {
            return bundles;
        }

        public void setBundles(ArrayList<Products> bundles) {
            this.bundles = bundles;
        }

        public Float getValucart_price() {
            return valucart_price;
        }

        public void setValucart_price(Float valucart_price) {
            this.valucart_price = valucart_price;
        }

        public String getStatus_string() {
            return status_string;
        }

        public void setStatus_string(String status_string) {
            this.status_string = status_string;
        }

        public Float getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(Float delivery_charge) {
            this.delivery_charge = delivery_charge;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}

