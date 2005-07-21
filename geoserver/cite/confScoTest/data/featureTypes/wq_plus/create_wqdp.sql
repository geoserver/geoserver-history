CREATE TABLE wq_ir_results ( anzlic_no                      
VARCHAR(20),
    project_no                     VARCHAR(20) NOT NULL,
    task_description               VARCHAR(60) NOT NULL,
    station_no                     VARCHAR(8),
    station_name                   VARCHAR(100),
    longitude                      real,
    latitude                       real,
    sample_collection_date         varchar(10),
    sample_type_description        VARCHAR(60),
    sample_method_description      VARCHAR(60),
    sample_purpose_description     VARCHAR(60),
    determinand_code               VARCHAR(20),
    results_value                  real,
    units_description              VARCHAR(60),
    determinand_description        VARCHAR(60)
    )