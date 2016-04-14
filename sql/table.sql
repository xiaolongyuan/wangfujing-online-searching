SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS online_sort_rule;
CREATE TABLE online_sort_rule (
  sid BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '排序规则编号',
  channel VARCHAR(32) COMMENT '渠道编码',
  order_field VARCHAR(64) COMMENT '排序字段',
  channel_default BOOLEAN DEFAULT FALSE COMMENT '该渠道默认排序',
  show_text VARCHAR(128) NOT NULL COMMENT '显示内容',
  show_order INT DEFAULT 0 COMMENT '显示顺序',
  default_order_by VARCHAR(4) NOT NULL COMMENT '默认排序规则(asc或desc)',
  other_order_by VARCHAR(4) COMMENT '另外一个排序规则(asc或desc)，可以为空，表示只有默认排序',
  UNIQUE INDEX uk_online_sort_rule(channel, order_field)
) COMMENT '排序规则表' DEFAULT CHARSET utf8 ENGINE InnoDB;

DROP TABLE IF EXISTS online_interval_content;
CREATE TABLE online_interval_content (
  sid BIGINT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '区间定义编号',
  field VARCHAR(20) NOT NULL COMMENT '字段名称',
  show_text VARCHAR(20) COMMENT '显示内容',
  channel VARCHAR(4) COMMENT '渠道', # DDD 修改管理接口 OSP
  selected BOOLEAN NOT NULL COMMENT '选择标志，同一channel只能有一条规则是selected，业务代码内保证',
  INDEX online_interval_content_selected(selected ASC),
  INDEX online_interval_content_channel(channel ASC)
) COMMENT '区间定义表' DEFAULT CHARSET utf8 ENGINE InnoDB;

DROP TABLE IF EXISTS online_interval_detail;
CREATE TABLE online_interval_detail (
  sid BIGINT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '区间明细编号',
  content_sid BIGINT(11) NOT NULL COMMENT '区间定义编号',
  lower_limit VARCHAR(20) COMMENT '下限',
  upper_limit VARCHAR(20) COMMENT '上限',
  order_by INT DEFAULT 0 COMMENT '顺序',
  show_text VARCHAR(50) COMMENT '展示值',
  INDEX online_interval_content_detail (content_sid ASC),
  INDEX online_interval_content_order_by (order_by ASC)
) COMMENT '区间明细表' DEFAULT CHARSET utf8 ENGINE InnoDB;

DROP TABLE IF EXISTS online_top_spot_brand;
CREATE TABLE online_top_spot_brand (
  sid BIGINT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '坑位编号',
  brand_id VARCHAR(128) NOT NULL COMMENT '品牌编号',
  spu_id VARCHAR(128) NOT NULL COMMENT 'SPU编号',
  orders INT DEFAULT 0 COMMENT '顺序，值越大越靠前',
  create_time TIMESTAMP COMMENT '创建时间',
  create_operator VARCHAR(50) COMMENT '创建操作人'
) COMMENT '品牌坑位（推广位）定义' DEFAULT CHARSET utf8 ENGINE InnoDB;

DROP TABLE IF EXISTS online_top_spot_brand_log;
CREATE TABLE online_top_spot_brand_log (
  sid BIGINT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '坑位编号',
  brand_id VARCHAR(128) NOT NULL COMMENT '品牌编号',
  spu_id VARCHAR(128) NOT NULL COMMENT 'SPU编号',
  orders INT DEFAULT 0 COMMENT '顺序',
  modify_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录修改时间',
  modifier VARCHAR(50) COMMENT '记录修改人',
  modify_type VARCHAR(20) NOT NULL COMMENT '操作类型：ADD/DEL/MOD'
) COMMENT '品牌坑位（推广位）定义记录' DEFAULT CHARSET utf8 ENGINE InnoDB;

DROP TABLE IF EXISTS online_top_spot_category;
CREATE TABLE online_top_spot_category (
  sid BIGINT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '坑位编号',
  category_id VARCHAR(128) NOT NULL COMMENT '分类编号',
  spu_id VARCHAR(128) NOT NULL COMMENT 'SPU编号',
  orders INT DEFAULT 0 COMMENT '顺序，值越大越靠前',
  create_time TIMESTAMP COMMENT '创建时间',
  create_operator VARCHAR(50) COMMENT '创建操作人'
) COMMENT '分类坑位（推广位）定义' DEFAULT CHARSET utf8 ENGINE InnoDB;

DROP TABLE IF EXISTS online_top_spot_category_log;
CREATE TABLE online_top_spot_category_log (
  sid BIGINT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '坑位编号',
  category_id VARCHAR(128) NOT NULL COMMENT '分类编号',
  spu_id VARCHAR(128) NOT NULL COMMENT 'SPU编号',
  orders INT DEFAULT 0 COMMENT '顺序',
  modify_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录修改时间',
  modifier VARCHAR(50) COMMENT '记录修改人',
  modify_type VARCHAR(20) NOT NULL COMMENT '操作类型：ADD/DEL/MOD'
) COMMENT '分类坑位（推广位）定义记录' DEFAULT CHARSET utf8 ENGINE InnoDB;

DROP TABLE IF EXISTS online_search_config;
CREATE TABLE online_search_config (
  channel VARCHAR(4) COMMENT '渠道', # DDD 修改管理接口 OSP
  name VARCHAR(50) NOT NULL COMMENT '配置名',
  value VARCHAR(1024) COMMENT '配置项值',
  PRIMARY KEY (channel, name),
  INDEX online_search_config_channel(channel ASC),
  INDEX online_search_config_name(name ASC)
) COMMENT '搜索配置表' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_index_config;
CREATE TABLE online_index_config (
  name VARCHAR(50) PRIMARY KEY COMMENT '配置名',
  value VARCHAR(1024) COMMENT '配置项值'
) COMMENT '索引配置表' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_index_blacklist;
CREATE TABLE online_index_blacklist (
  id VARCHAR(50) NOT NULL COMMENT '禁用记录的编码',
  type VARCHAR(20) NOT NULL COMMENT '禁用记录的类型 ITEM/SKU/SPU/BRAND',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  creator VARCHAR(50) COMMENT '创建者',
  PRIMARY KEY (id, type)
) COMMENT '索引黑名单' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_index_blacklist_record;
CREATE TABLE online_index_blacklist_record (
  sid BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '序列号',
  id VARCHAR(50) NOT NULL COMMENT '禁用的编码',
  type VARCHAR(20) NOT NULL COMMENT '禁用的类型 ITEM/SKU/SPU/BRAND',
  modify_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录修改时间',
  modifier VARCHAR(50) COMMENT '记录修改人',
  modify_type VARCHAR(20) NOT NULL COMMENT '操作类型：ADD/DEL/MOD'
) COMMENT '索引黑名单操作记录' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_index_manual_boost;
CREATE TABLE online_index_manual_boost (
  sku_id VARCHAR(50) NOT NULL PRIMARY KEY COMMENT '加权的sku编码',
  boost FLOAT NOT NULL DEFAULT 1 COMMENT '加权数'
) COMMENT '手工加权表' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_statistics_sales_volume;
CREATE TABLE online_statistics_sales_volume (
  sid BIGINT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '序号',
  item_id VARCHAR(50) NOT NULL COMMENT '专柜商品编码',
  sku_id VARCHAR(50) NOT NULL COMMENT 'SKU编码',
  spu_id VARCHAR(50) NOT NULL COMMENT 'SPU编码',
  sales INT NOT NULL COMMENT '此次售出数量',
  sale_time DATETIME NOT NULL COMMENT '出售时间',
  INDEX online_statistics_sales_volume_idx_item_id(item_id ASC),
  INDEX online_statistics_sales_volume_idx_sku_id(sku_id ASC),
  INDEX online_statistics_sales_volume_idx_spu_id(spu_id ASC),
  INDEX online_statistics_sales_volume_idx_sale_time(sale_time DESC)
) COMMENT '线上销量统计表' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_statistics_clicks;
CREATE TABLE online_statistics_clicks (
  sid BIGINT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '序号',
  tid VARCHAR(50) NOT NULL COMMENT 'track Cookie',
  pid BIGINT(20) NULL COMMENT '跳出列表页URL sid',
  spu_id VARCHAR(50) NOT NULL COMMENT 'SPU编码',
  click_time DATETIME NOT NULL COMMENT '时间',
  INDEX online_statistics_clicks_idx_tid(tid ASC),
  INDEX online_statistics_clicks_idx_pid(pid ASC),
  INDEX online_statistics_clicks_idx_spu_id(spu_id ASC),
  INDEX online_statistics_clicks_idx_time(click_time DESC)
) COMMENT '线上列表跳出点击统计表' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_statistics_url_dict;
CREATE TABLE online_statistics_url_dict (
  sid BIGINT(20) PRIMARY KEY AUTO_INCREMENT COMMENT '序号',
  url VARCHAR(5120) NOT NULL COMMENT 'url',
  INDEX online_statistics_url_dict_url(url ASC)
) COMMENT '线上URL字典' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_retry_note;
CREATE TABLE online_retry_note (
  sid BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL COMMENT '编码',
  resolved BOOLEAN DEFAULT FALSE COMMENT '失败创建是否解决',
  step VARCHAR(16) DEFAULT 'unknown' COMMENT '失败位置：pcm、es、index、success',
  type VARCHAR(16) DEFAULT 'unknown' COMMENT '重试类别：category、brand、spu、sku、item、comment',
  action VARCHAR(16) DEFAULT 'unknown' COMMENT '失败操作的类型：create、delete',
  version BIGINT DEFAULT 0 COMMENT '乐观锁标记',
  update_time TIMESTAMP COMMENT '该记录修改时间',
  comment VARCHAR(256) COMMENT '备注',
  INDEX online_retry_node_step(step ASC),
  INDEX online_retry_node_type(type ASC),
  INDEX online_retry_node_action(action ASC)
) COMMENT '线上-重试补偿记录表' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS online_retry_note_backup;
CREATE TABLE online_retry_note_backup(
  sid BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL COMMENT '编码',
  resolved BOOLEAN DEFAULT FALSE COMMENT '失败创建是否解决',
  step VARCHAR(16) DEFAULT 'unknown' COMMENT '失败位置：pcm、es、index、success',
  type VARCHAR(16) DEFAULT 'unknown' COMMENT '重试类别：category、brand、spu、sku、item、comment',
  action VARCHAR(16) DEFAULT 'unknown' COMMENT '失败操作的类型：create、delete',
  version BIGINT DEFAULT 0 COMMENT '乐观锁标记',
  update_time TIMESTAMP COMMENT '该记录修改时间',
  comment VARCHAR(256) COMMENT '备注'
) COMMENT '线上-重试补偿完成备份表' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS hot_word;
CREATE TABLE hot_word (
  sid BIGINT PRIMARY KEY AUTO_INCREMENT,
  site VARCHAR(32) NOT NULL COMMENT '所在站点',
  channel VARCHAR(32) NOT NULL COMMENT '所在频道',
  value VARCHAR(255) NOT NULL COMMENT '显示内容',
  link TEXT NOT NULL COMMENT '热词链接',
  orders INT DEFAULT 0 COMMENT '展示顺序',
  enabled BOOLEAN DEFAULT FALSE COMMENT '是否有效：true有效，false无效'
) COMMENT '热词表' ENGINE innoDB DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS hot_word_record;
CREATE TABLE hot_word_record (
  sid BIGINT PRIMARY KEY AUTO_INCREMENT,
  hot_word_sid BIGINT COMMENT '热词表id',
  site VARCHAR(32) NOT NULL COMMENT '所在站点',
  channel VARCHAR(32) NOT NULL COMMENT '所在频道',
  value VARCHAR(255) NOT NULL COMMENT '显示内容',
  link TEXT NOT NULL COMMENT '热词链接',
  orders INT DEFAULT 0 COMMENT '展示顺序',
  enabled BOOLEAN DEFAULT FALSE COMMENT '是否有效：true有效，false无效',
  modifier VARCHAR(512) NOT NULL COMMENT '修改人',
  modify_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  modify_type VARCHAR(16) COMMENT '操作类型：ADD新建/DEL删除/MOD修改/ENABLED使生效/DISABLED使失效'
) COMMENT '热词操作记录表' ENGINE innoDB DEFAULT CHARSET utf8;

SET FOREIGN_KEY_CHECKS = 1;
