package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ByobSummary implements Serializable {

    int status;
    String message;
    public Data data = new Data();

    public static class Data implements Serializable {

        public ArrayList<Products> products = new ArrayList<>();
        Float total_price,price,discount,discounted_price,maximum_selling_price,valucart_price;
        int id,cart_id;
        String name;
        public ArrayList<Products> customer_bundles = new ArrayList<>();

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

        public Float getTotal_price() {
            return total_price;
        }

        public void setTotal_price(Float total_price) {
            this.total_price = total_price;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public ArrayList<Products> getCustomer_bundles() {
            return customer_bundles;
        }

        public void setCustomer_bundles(ArrayList<Products> customer_bundles) {
            this.customer_bundles = customer_bundles;
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

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

