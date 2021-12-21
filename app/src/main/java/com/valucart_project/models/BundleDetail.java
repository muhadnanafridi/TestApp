package com.valucart_project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BundleDetail implements Serializable ,Cloneable{

    public Data data = new Data();
    public BundleDetail clone()throws CloneNotSupportedException{
        return  (BundleDetail)  super.clone();
    }

    public static class Data implements Serializable ,Cloneable {

        String id,category,name,description;
        int item_count;
        Float maximum_selling_price,valucart_price,percentage_discount;
        Boolean is_popular;
        ArrayList<String> images = new ArrayList<>();
        public ArrayList<Products> products = new ArrayList<>();
        int status;

        public static class Products  implements Serializable ,Cloneable {
            int quantity;
            public ProductData data = new ProductData();
            public ArrayList<ProductsAlternatives> alternatives = new ArrayList<>();

            public static class ProductData implements Serializable ,Cloneable {

                String id, name, description, thumbnail;
                Float maximum_selling_price, valucart_price, packaging_quantity;
                Boolean IsSelected=false;
                ArrayList<String> images = new ArrayList<>();
                PackagingQuantity packaging_quantity_unit = new PackagingQuantity();

                public Boolean getSelected() {
                    return IsSelected;
                }

                public void setSelected(Boolean selected) {
                    IsSelected = selected;
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

                public Float getPackaging_quantity() {
                    return packaging_quantity;
                }

                public void setPackaging_quantity(Float packaging_quantity) {
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
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public ProductData getData() {
                return data;
            }

            public void setData(ProductData data) {
                this.data = data;
            }

            public ArrayList<ProductsAlternatives> getAlternatives() {
                return alternatives;
            }

            public void setAlternatives(ArrayList<ProductsAlternatives> alternatives) {
                this.alternatives = alternatives;
            }

            public static class ProductsAlternatives  implements Serializable ,Cloneable {
                int quantity;
                public ProductData data = new ProductData();

                public static class ProductData implements Serializable ,Cloneable {

                    String id, name, description, thumbnail;
                    Float maximum_selling_price, valucart_price, packaging_quantity;
                    Boolean IsSelected=false;
                    ArrayList<String> images = new ArrayList<>();
                    Data.Products.ProductData.PackagingQuantity packaging_quantity_unit = new Data.Products.ProductData.PackagingQuantity();

                    public Boolean getSelected() {
                        return IsSelected;
                    }

                    public void setSelected(Boolean selected) {
                        IsSelected = selected;
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

                    public Float getPackaging_quantity() {
                        return packaging_quantity;
                    }

                    public void setPackaging_quantity(Float packaging_quantity) {
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

                    public Data.Products.ProductData.PackagingQuantity getPackaging_quantity_unit() {
                        return packaging_quantity_unit;
                    }

                    public void setPackaging_quantity_unit(Data.Products.ProductData.PackagingQuantity packaging_quantity_unit) {
                        this.packaging_quantity_unit = packaging_quantity_unit;
                    }

                    public String getThumbnail() {
                        return thumbnail;
                    }

                    public void setThumbnail(String thumbnail) {
                        this.thumbnail = thumbnail;
                    }
                }

                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }

                public ProductData getData() {
                    return data;
                }

                public void setData(ProductData data) {
                    this.data = data;
                }
            }


        }

        public ArrayList<Products> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<Products> products) {
            this.products = products;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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

        public int getItem_count() {
            return item_count;
        }

        public void setItem_count(int item_count) {
            this.item_count = item_count;
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

        public Float getPercentage_discount() {
            return percentage_discount;
        }

        public void setPercentage_discount(Float percentage_discount) {
            this.percentage_discount = percentage_discount;
        }

        public Boolean getIs_popular() {
            return is_popular;
        }

        public void setIs_popular(Boolean is_popular) {
            this.is_popular = is_popular;
        }

        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
