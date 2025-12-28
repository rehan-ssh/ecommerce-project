CREATE TABLE category
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    is_deleted    BIT(1) NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE category_featured_products
(
    category_id          BIGINT NOT NULL,
    featured_products_id BIGINT NOT NULL
);

CREATE TABLE product
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    is_deleted    BIT(1) NULL,
    `description` VARCHAR(255) NULL,
    price DOUBLE NOT NULL,
    image_url     VARCHAR(255) NULL,
    category_id   BIGINT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

ALTER TABLE category_featured_products
    ADD CONSTRAINT uc_category_featured_products_featuredproducts UNIQUE (featured_products_id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE category_featured_products
    ADD CONSTRAINT fk_catfeapro_on_category FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE category_featured_products
    ADD CONSTRAINT fk_catfeapro_on_product FOREIGN KEY (featured_products_id) REFERENCES product (id);