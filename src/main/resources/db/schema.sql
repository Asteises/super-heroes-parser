CREATE TABLE IF NOT EXISTS Hero_page
(
    id             UUID PRIMARY KEY,
    url            VARCHAR,
    title          VARCHAR,
    suffix_level_1 VARCHAR,
    suffix_level_2 VARCHAR
);

CREATE TABLE IF NOT EXISTS Heroes
(
    id               UUID PRIMARY KEY,
    url              VARCHAR,
    content          VARCHAR,
    portrait_url     VARCHAR,
    h_1_name         VARCHAR,
    h_2_name         VARCHAR,
    solar_system     VARCHAR,
    creator          VARCHAR,
    universe         VARCHAR,
    full_name        VARCHAR,
    aliases          VARCHAR,
    place_of_birth   VARCHAR,
    first_appearance VARCHAR,
    alignment        VARCHAR,
    intelligence     VARCHAR,
    strength         VARCHAR,
    speed            VARCHAR
);

CREATE TABLE IF NOT EXISTS Tabs
(
    id      UUID PRIMARY KEY,
    title   VARCHAR,
    url     VARCHAR,
    hero_id UUID,
    FOREIGN KEY (hero_id) REFERENCES Heroes (id)
);

CREATE TABLE IF NOT EXISTS Main_powers
(
    id      UUID PRIMARY KEY,
    name    VARCHAR,
    hero_id UUID,
    FOREIGN KEY (hero_id) REFERENCES Heroes (id)
);

CREATE TABLE IF NOT EXISTS Powers
(
    id          UUID PRIMARY KEY,
    url         VARCHAR,
    name        VARCHAR,
    tier        VARCHAR,
    score       VARCHAR,
    description VARCHAR
);

CREATE TABLE IF NOT EXISTS Heroes_powers
(
    hero_id  UUID NOT NULL,
    power_id UUID NOT NULL,
    CONSTRAINT pk_heroes_powers PRIMARY KEY (hero_id, power_id)
);