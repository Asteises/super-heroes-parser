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
    id           UUID PRIMARY KEY,
    url          VARCHAR,
    hero_page_id UUID,
    FOREIGN KEY (hero_page_id) references Heroes (id)
);

CREATE TABLE IF NOT EXISTS Powers
(
    id          UUID PRIMARY KEY,
    url         VARCHAR,
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