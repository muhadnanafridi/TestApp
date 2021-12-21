package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerBundles implements Serializable {

    public ArrayList<Data> data = new ArrayList<>();
    int item_count;
    public Meta meta = new Meta();

    public static class Data implements Serializable {

        public ArrayList<Products> products = new ArrayList<>();
        int id,customer_id;
        String name,description,created_at;
        Float price,discount,discounted_price,valucart_price,maximum_selling_price;

        public static class Products  implements Serializable {

            int quantity;
            public DataProduct product = new DataProduct();

            public static class DataProduct implements Serializable {

                int subcategory_id;
                String id, name, description , thumbnail,packaging_quantity;
                Float maximum_selling_price, valucart_price,price;
                int addToByob = 0;
                ArrayList<String> images = new ArrayList<>();
                PackagingQuantity packaging_quantity_unit = new PackagingQuantity();

                public Float getPrice() {
                    return price;
                }

                public void setPrice(Float price) {
                    this.price = price;
                }

                public String getThumbnail() {
                    return thumbnail == null ? "" : thumbnail;
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

                public static class PackagingQuantity implements Serializable {

                    String id, name, symbol;

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
                        return symbol == null ? "" : symbol;
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

            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public DataProduct getProduct() {
                return product;
            }

            public void setProduct(DataProduct product) {
                this.product = product;
            }

        }

        public ArrayList<Products> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<Products> products) {
            this.products = products;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public Float getDiscount() {
            return discount;
        }

        public void setDiscount(Float discount) {
            this.discount = discount;
        }

        public Float getDiscounted_price() {
            return discounted_price;
        }

        public void setDiscounted_price(Float discounted_price) {
            this.discounted_price = discounted_price;
        }

        public Float getValucart_price() {
            return valucart_price;
        }

        public void setValucart_price(Float valucart_price) {
            this.valucart_price = valucart_price;
        }

        public Float getMaximum_selling_price() {
            return maximum_selling_price;
        }

        public void setMaximum_selling_price(Float maximum_selling_price) {
            this.maximum_selling_price = maximum_selling_price;
        }
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
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

