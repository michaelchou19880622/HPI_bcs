-- ------------------------------------------------------------------------------
-- 刪除所有 foreign key ，收尋出來後用 replace all ' ，再執行

SELECT CONCAT('ALTER TABLE ',TABLE_SCHEMA,'.',TABLE_NAME,' DROP FOREIGN KEY ',CONSTRAINT_NAME,' ;') 
FROM information_schema.TABLE_CONSTRAINTS c 
WHERE c.TABLE_SCHEMA='bcs' AND c.CONSTRAINT_TYPE='FOREIGN KEY';

-- ------------------------------------------------------------------------------
-- 以下建表資訊

DROP TABLE IF EXISTS `autoreply` ;
CREATE TABLE `autoreply` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `keyword` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '主要關鍵字，僅供列表用',
  `type` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '回覆類型:關鍵字，黑名單等',
  `status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE : 啟用\nDISABLE: 停用\nDELETE: 刪除',
  `user_status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '綁定狀態\nall : 全部\nbinded : 綁定\nunbinded 未綁定\n',
  `reply_index` tinyint(4) NOT NULL DEFAULT '0' COMMENT '順位\n',
  `period` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '發送時間類型\nforever :　永久\nDay : 日期期間 datetime_begin:datetime_end( 2018/04/27 00:00:00~2018/04/27 23:59:59)\nTime : 時間',
  `datetime_begin` datetime DEFAULT NULL COMMENT '發送起始時間',
  `datetime_end` datetime DEFAULT NULL COMMENT '發送結束時間',
  `response_count` int(11) DEFAULT '0' COMMENT '回覆數量',
  `memo` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '備註(目前不使用)',
  `modify_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  `lineusergroup_id` bigint(20) DEFAULT '0',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modification_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='自動回覆主檔，回覆類型有關鍵字，互動，黑名單關鍵字等';

DROP TABLE IF EXISTS `autoreply_autoreply_message_list` ;
CREATE TABLE `autoreply_autoreply_message_list` (
  `autoreply_id` bigint(20) NOT NULL,
  `autoreply_message_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `autoreplymessagelistid_idx` (`autoreply_message_list_id`),
  KEY `autoreplyid_idx` (`autoreply_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `autoreply_id_seq` ;
CREATE TABLE `autoreply_id_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into autoreply_id_seq (next_val) values (1);

DROP TABLE IF EXISTS `autoreply_detail` ;
CREATE TABLE `autoreply_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `autoreply_id` bigint(20) unsigned NOT NULL COMMENT '對應AUTOREPLY的識別碼',
  `keyword` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '關鍵字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='自動回覆的多關鍵字，存放其他關鍵字';

DROP TABLE IF EXISTS `autoreply_detail_id_seq` ;
CREATE TABLE `autoreply_detail_id_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into autoreply_detail_id_seq (next_val) values (1);

DROP TABLE IF EXISTS `autoreply_message_list` ;
CREATE TABLE `autoreply_message_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `autoreply_id` bigint(20) unsigned NOT NULL,
  `message_id` bigint(20) NOT NULL COMMENT '訊息識別碼對應至相關訊息的ID\n',
  `message_type` varchar(20) NOT NULL COMMENT '訊息類別(如 TEXT, IMAGE等)',
  `order_num` int(11) NOT NULL DEFAULT '999' COMMENT '訊息發送順序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='自動回覆的訊息內容索引檔';

DROP TABLE IF EXISTS `autoreply_message_list_message_audio_list` ;
CREATE TABLE `autoreply_message_list_message_audio_list` (
  `autoreply_message_list_id` bigint(20) NOT NULL,
  `message_audio_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messageaudiolistid_idx` (`message_audio_list_id`),
  KEY `autoreplymessagelistid_idx` (`autoreply_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `autoreply_message_list_message_image_list` ;
CREATE TABLE `autoreply_message_list_message_image_list` (
  `autoreply_message_list_id` bigint(20) NOT NULL,
  `message_image_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messageimagelistid_idx` (`message_image_list_id`),
  KEY `autoreplymessagelistid_idx1` (`autoreply_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `autoreply_message_list_message_image_map_list` ;
CREATE TABLE `autoreply_message_list_message_image_map_list` (
  `autoreply_message_list_id` bigint(20) NOT NULL,
  `message_image_map_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messageimagemaplistid_idx` (`message_image_map_list_id`),
  KEY `autoreplymessagelistid_idx2` (`autoreply_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `autoreply_message_list_message_sticker_list` ;
CREATE TABLE `autoreply_message_list_message_sticker_list` (
  `autoreply_message_list_id` bigint(20) NOT NULL,
  `message_sticker_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messagestickerlistid_idx` (`message_sticker_list_id`),
  KEY `autoreplymessagelistid_idx3` (`autoreply_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `autoreply_message_list_message_template_list` ;
CREATE TABLE `autoreply_message_list_message_template_list` (
  `autoreply_message_list_id` bigint(20) NOT NULL,
  `message_template_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messagetemplatelistid_idx` (`message_template_list_id`),
  KEY `autoreplymessagelistid_idx4` (`autoreply_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `autoreply_message_list_message_text_list` ;
CREATE TABLE `autoreply_message_list_message_text_list` (
  `autoreply_message_list_id` bigint(20) NOT NULL,
  `message_text_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messagetextlistid_idx` (`message_text_list_id`),
  KEY `autoreplymessagelistid_idx5` (`autoreply_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `autoreply_message_list_message_video_list` ;
CREATE TABLE `autoreply_message_list_message_video_list` (
  `autoreply_message_list_id` bigint(20) NOT NULL,
  `message_video_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messagevideolistid_idx` (`message_video_list_id`),
  KEY `autoreplymessagelistid_idx6` (`autoreply_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bcs_content_resource` ;
CREATE TABLE `bcs_content_resource` (
  `resource_id` varchar(50) NOT NULL,
  `content_type` varchar(50) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `modify_user` varchar(50) DEFAULT NULL,
  `resource_height` bigint(20) DEFAULT NULL,
  `resource_length` int(11) DEFAULT NULL,
  `resource_preview` varchar(50) DEFAULT NULL,
  `resource_size` bigint(20) DEFAULT NULL,
  `resource_title` varchar(50) DEFAULT NULL,
  `resource_type` varchar(50) DEFAULT NULL,
  `resource_width` bigint(20) DEFAULT NULL,
  `use_flag` bit(1) DEFAULT NULL,
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `distinct_attribute` ;
CREATE TABLE `distinct_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filepath` varchar(255) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event` ;
CREATE TABLE `event` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_prize_id` bigint(20) DEFAULT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'DEFAULT',
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_status` varchar(20) COLLATE utf8_unicode_ci DEFAULT 'ALL',
  `begin_datetime` datetime DEFAULT NULL,
  `end_datetime` datetime DEFAULT NULL,
  `input_error_time` int(11) DEFAULT '0',
  `input_timeout` int(11) DEFAULT '0',
  `modify_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `attendance_count` int(11) DEFAULT NULL,
  `minimum_spend_amount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `modifyuser_idx` (`modify_user`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_applydata` ;
CREATE TABLE `event_applydata` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(50) NOT NULL,
  `column_name` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `column_key` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `column_format` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'F0001: 英文\nF0002: 數字\nF0003: 英數字\nF0004 英數字加特殊符號(非中文)\nF0011: 中文	\nF0012: 中文英數字\nF0021: email\nF0022: cellphone\nF0023 生日\nF0024性別\n',
  `column_length` int(11) DEFAULT NULL,
  `order_index` int(11) NOT NULL,
  `status` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `keyword` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `eventid_idx` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_applydata_keyword` ;
CREATE TABLE `event_applydata_keyword` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_applydata_id` bigint(50) DEFAULT NULL,
  `keyword` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `keyword_event` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `eventapplydataid_idx` (`event_applydata_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_applydata_keyword_message_list` ;
CREATE TABLE `event_applydata_keyword_message_list` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_applydata_keyword_id` bigint(50) DEFAULT NULL,
  `message_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `eventapplydatakeywordid_idx` (`event_applydata_keyword_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_attendance` ;
CREATE TABLE `event_attendance` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(50) NOT NULL,
  `lineuser_id` bigint(50) NOT NULL,
  `apply_date` datetime DEFAULT NULL,
  `apply_status` int(11) DEFAULT NULL,
  `get_ticket` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `get_ticket_datetime` datetime DEFAULT NULL,
  `checkin_date` datetime DEFAULT NULL,
  `checkin_employee` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `getprize_date` datetime DEFAULT NULL,
  `getprize_employee` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `input_error_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `eventid_idx` (`event_id`),
  KEY `lineuserid_idx` (`lineuser_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_attendance_attribute` ;
CREATE TABLE `event_attendance_attribute` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) DEFAULT NULL,
  `lineuser_id` bigint(20) DEFAULT NULL,
  `event_attendance_id` bigint(50) DEFAULT NULL,
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `attr_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `attr_value` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `format_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `eventattendanceid_idx` (`event_attendance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_attendance_confirm_time` ;
CREATE TABLE `event_attendance_confirm_time` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) DEFAULT NULL,
  `expired_time` datetime DEFAULT NULL,
  `lineuser_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_attendance_event_attendance_attribute` ;
CREATE TABLE `event_attendance_event_attendance_attribute` (
  `event_attendance_id` bigint(20) NOT NULL,
  `event_attendance_attribute_id` bigint(20) NOT NULL,
  UNIQUE KEY `eventattendanceattributeid_idx` (`event_attendance_attribute_id`),
  KEY `eventattendanceid_idx` (`event_attendance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_attendance_logs` ;
CREATE TABLE `event_attendance_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) NOT NULL,
  `lineuser_id` bigint(20) NOT NULL,
  `apply_status` int(11) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_beacon` ;
CREATE TABLE `event_beacon` (
  `replyToken` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `hwid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `type` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `dm` varchar(50) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_content` ;
CREATE TABLE `event_content` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '活動，通路主檔',
  `event_id` bigint(20) DEFAULT NULL,
  `modify_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_content_0820` ;
CREATE TABLE `event_content_0820` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) DEFAULT NULL,
  `total_price` int(11) DEFAULT NULL COMMENT '滿額價',
  `reward_point` int(11) DEFAULT NULL,
  `content_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '活動內容類型\nITEM:    品項給點\nPRICE: 滿額給點',
  `modify_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `eventid_idx` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_content_detail_0820` ;
CREATE TABLE `event_content_detail_0820` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_content_id` int(10) unsigned DEFAULT NULL,
  `retailer_id` int(11) DEFAULT NULL,
  `goods_id` int(11) DEFAULT NULL,
  `reward_point` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_retailerid_content_idx` (`retailer_id`),
  KEY `fk_goods_content_idx` (`goods_id`),
  KEY `fk_content_main_idx` (`event_content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_goods` ;
CREATE TABLE `event_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) NOT NULL,
  `goods_id` bigint(20) NOT NULL,
  `reward_point` int(11) NOT NULL,
  `modify_user` varchar(20) DEFAULT NULL,
  `modify_datetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_invoice_attendance` ;
CREATE TABLE `event_invoice_attendance` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(50) NOT NULL,
  `lineuser_id` bigint(50) NOT NULL,
  `apply_date` datetime DEFAULT NULL,
  `apply_status` int(11) DEFAULT NULL,
  `input_error_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `eventid_idx` (`event_id`),
  KEY `lineuserid_idx` (`lineuser_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_keyword` ;
CREATE TABLE `event_keyword` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(50) NOT NULL,
  `keyword` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `keyword_event` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `eventid_idx` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_keyword_message_list` ;
CREATE TABLE `event_keyword_message_list` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_keyword_id` bigint(50) NOT NULL,
  `message_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message_id` int(11) DEFAULT NULL,
  `order_index` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `eventkeywordid_idx` (`event_keyword_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_postback` ;
CREATE TABLE `event_postback` (
  `replytoken` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `data` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mode` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `param` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_prize`;
CREATE TABLE `event_prize` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `event_id` bigint(50) DEFAULT NULL COMMENT '對應到 event 表的 id',
  `prize_id` bigint(50) DEFAULT NULL COMMENT '對應到 product 表的 id',
  `prize_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '表示 需要對應的 表來源',
  `announce_datetime` datetime DEFAULT NULL COMMENT '有效的開始時間',
  `expired_datetime` datetime DEFAULT NULL COMMENT '有效的結束時間',
  `volume` int(11) DEFAULT NULL COMMENT '數量',
  `next_event_prize_id` bigint(20) DEFAULT NULL COMMENT '贈品券的 id，對應到 event_prize 表的 id',
  `questionnaire_main_id` bigint(50) DEFAULT NULL COMMENT '對應到 questionnaire_main 的 id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_prize_detail`;
CREATE TABLE `event_prize_detail` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `event_prize_id` bigint(50) DEFAULT NULL COMMENT '對應到 event_prize 表的 id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '對應到 store 表的 id',
  `prize_sn` varchar(33) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '該票券的序號',
  `used_lineuser_id` bigint(50) DEFAULT NULL COMMENT '對應到 lineuser 表的 id',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'N' COMMENT '使用的狀態，0：未使用、1：已使用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_retailer` ;
CREATE TABLE `event_retailer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) NOT NULL,
  `retailer_id` bigint(20) NOT NULL,
  `modify_user` varchar(20) DEFAULT NULL,
  `modify_datetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event_reward_card` ;
CREATE TABLE `event_reward_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) NOT NULL,
  `reward_card_id` bigint(20) NOT NULL,
  `modify_user` varchar(20) DEFAULT NULL,
  `modify_datetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fake_flex_message` ;
CREATE TABLE `fake_flex_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_prize_id` bigint(50) DEFAULT NULL,
  `content` longtext COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message` ;
CREATE TABLE `flex_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(10) NOT NULL COMMENT 'BUBBLE_CAROUSEL',
  `alt_text` varchar(400) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_box_component_list` ;
CREATE TABLE `flex_message_box_component_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `box_id` bigint(20) NOT NULL,
  `component_id` bigint(20) NOT NULL,
  `component_type` varchar(20) NOT NULL,
  `order_index` int(3) NOT NULL DEFAULT '999',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_box_container` ;
CREATE TABLE `flex_message_box_container` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `layout` varchar(20) NOT NULL,
  `flex` int(10) DEFAULT NULL,
  `spacing` varchar(10) DEFAULT NULL,
  `margin` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_box_container` ;
CREATE TABLE `flex_message_box_container` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `layout` varchar(20) NOT NULL,
  `flex` int(10) DEFAULT NULL,
  `spacing` varchar(10) DEFAULT NULL,
  `margin` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_bubble_component_list` ;
CREATE TABLE `flex_message_bubble_component_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bubble_id` bigint(20) NOT NULL,
  `bubble_type` varchar(20) NOT NULL,
  `component_id` bigint(20) NOT NULL,
  `component_type` varchar(20) NOT NULL,
  `order_index` int(3) DEFAULT '999',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_bubble_container` ;
CREATE TABLE `flex_message_bubble_container` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `flex_id` bigint(20) NOT NULL,
  `direction` varchar(10) DEFAULT 'ltr',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_button_component` ;
CREATE TABLE `flex_message_button_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `flex` int(10) DEFAULT NULL,
  `margin` varchar(10) DEFAULT NULL,
  `height` varchar(10) DEFAULT NULL,
  `style` varchar(10) DEFAULT NULL,
  `color` varchar(10) DEFAULT NULL,
  `gravity` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_carousel` ;
CREATE TABLE `flex_message_carousel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `flex_id` bigint(20) NOT NULL,
  `bubble_id` bigint(20) NOT NULL,
  `order_index` int(3) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_component_action` ;
CREATE TABLE `flex_message_component_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `component_id` bigint(20) NOT NULL,
  `component_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'MESSAGE' COMMENT 'POSTBACK / MESSAGE / URI',
  `label` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `data` varchar(300) COLLATE utf8_unicode_ci DEFAULT '',
  `text` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `uri` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_icon_component` ;
CREATE TABLE `flex_message_icon_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `margin` varchar(10) DEFAULT NULL,
  `size` varchar(10) DEFAULT NULL,
  `aspect_ratio` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_image_component` ;
CREATE TABLE `flex_message_image_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `flex` int(10) DEFAULT NULL,
  `margin` varchar(10) DEFAULT NULL,
  `align` varchar(10) DEFAULT 'CENTER',
  `gravity` varchar(10) DEFAULT 'TOP',
  `size` varchar(10) DEFAULT 'MD',
  `aspect_ratio` varchar(10) DEFAULT '1:1',
  `aspect_mode` varchar(10) DEFAULT 'Fit',
  `background_color` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_separator_component` ;
CREATE TABLE `flex_message_separator_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `margin` varchar(10) DEFAULT NULL,
  `color` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_spacer_component` ;
CREATE TABLE `flex_message_spacer_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `size` varchar(10) DEFAULT 'MD',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flex_message_text_component` ;
CREATE TABLE `flex_message_text_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(300) NOT NULL,
  `flex` int(10) DEFAULT NULL,
  `margin` varchar(10) DEFAULT NULL,
  `size` varchar(10) DEFAULT 'Md',
  `align` varchar(10) DEFAULT 'START',
  `gravity` varchar(10) DEFAULT NULL,
  `wrap` tinyint(4) DEFAULT '0',
  `weight` varchar(10) DEFAULT 'REGULAR',
  `color` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ------------------------------------------------------------------------------
-- Good

DROP TABLE IF EXISTS `goods` ;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `modify_datetime` datetime DEFAULT NULL,
  `modify_user` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `goods_detail` ;
CREATE TABLE `goods_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `goodsid_idx` (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hpi_level_activity`;
CREATE TABLE `hpi_level_activity` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `lineuser_id` bigint(50) DEFAULT NULL COMMENT '對應到 lineuser 表的 id',
  `level` int(11) DEFAULT NULL COMMENT '關卡的級別',
  `create_time` datetime DEFAULT NULL COMMENT '生成的時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hibernate_sequence` ;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into hibernate_sequence (next_val) values (1);

DROP TABLE IF EXISTS `invoice` ;
CREATE TABLE `invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `error_count` int(11) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `inv_date` datetime DEFAULT NULL,
  `inv_image_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `inv_num` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `inv_period` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `inv_random` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `inv_status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `inv_term` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lineuser_id` bigint(20) DEFAULT NULL,
  `msg` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `seller_address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `seller_ban` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `seller_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `upload_time` datetime DEFAULT NULL,
  `v` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `invoice_detail` ;
CREATE TABLE `invoice_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `invoice_id` bigint(20) DEFAULT NULL,
  `is_match` bit(1) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `invoiceid_idx` (`invoice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `liff`;
CREATE TABLE `liff` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `liff_id` varchar(50) DEFAULT NULL COMMENT 'Line 回傳的 id ',
  `type` varchar(10) DEFAULT NULL COMMENT '註冊的類型',
  `url` varchar(200) DEFAULT NULL COMMENT '註冊的網址',
  `modify_time` datetime DEFAULT NULL COMMENT '修改時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='保存註冊 liff 的資訊';

DROP TABLE IF EXISTS `lineuser` ;
CREATE TABLE `lineuser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `line_uid` char(33) NOT NULL,
  `status` int(11) NOT NULL,
  `tag` varchar(20) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `linked` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_attribute` ;
CREATE TABLE `lineuser_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lineuser_id` bigint(20) DEFAULT NULL,
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '欄位名稱',
  `attr_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `attr_value` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `format_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuserattribute` ;
CREATE TABLE `lineuserattribute` (
  `line_uid` varchar(255) NOT NULL,
  `birthday` date DEFAULT NULL,
  `cellphone` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`line_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_belonging` ;
CREATE TABLE `lineuser_belonging` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `lineuser_id` bigint(50) NOT NULL,
  `belonging_id` bigint(50) DEFAULT NULL,
  `belonging_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `belonging_source` bigint(50) DEFAULT NULL,
  `status` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `received_time` datetime DEFAULT NULL,
  `used_time` datetime DEFAULT NULL,
  `store_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_group` ;
CREATE TABLE `lineuser_group` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `modify_account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineusergroup` ;
CREATE TABLE `lineusergroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `getusers` varchar(2000) DEFAULT NULL,
  `modify_account` varchar(20) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `system_flag` char(1) DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
INSERT INTO `lineusergroup` (`id`,`name`,`description`,`getusers`,`modify_account`,`modify_time`,`system_flag`) VALUES (1,'全部使用者','全部使用者群組(一般, 已串聯), 不可刪除','SELECT LINE_UID FROM BCS.LINEUSER WHERE  ID IN (SELECT DISTINCT LINEUSER_ID FROM BCS.LINEUSER_ATTRIBUTE WHERE ATTR_KEY = \'gender\' AND ATTR_VALUE =\'Male\') ;','admin','2018-05-01 00:00:00','Y');
INSERT INTO `lineusergroup` (`id`,`name`,`description`,`getusers`,`modify_account`,`modify_time`,`system_flag`) VALUES (2,'已串聯使用者','已串聯使用者群組, 不可刪除','SELECT LINE_UID FROM BCS.LINEUSER WHERE  ID IN (SELECT DISTINCT LINEUSER_ID FROM BCS.LINEUSER_ATTRIBUTE WHERE ATTR_KEY = \'birthday\' AND YEAR(ATTR_VALUE) BETWEEN 2000 and 2017 ) ;','admin','2018-05-01 00:00:00','Y');
INSERT INTO `lineusergroup` (`id`,`name`,`description`,`getusers`,`modify_account`,`modify_time`,`system_flag`) VALUES (3,'一般使用者','一般使用者群組, 不可刪除','SELECT LINE_UID FROM BCS.LINEUSER WHERE ;','admin','2018-05-01 00:00:00','Y');
INSERT INTO `lineusergroup` (`id`,`name`,`description`,`getusers`,`modify_account`,`modify_time`,`system_flag`) VALUES (4,'內部測試群組','內部測試群組','select id FROM bcs.lineuser where line_uid in (SELECT lineuser_uid FROM bcs.systemuser where length(lineuser_uid)=33)','admin','2018-05-01 00:00:00','N');

DROP TABLE IF EXISTS `lineuser_group_condition` ;
CREATE TABLE `lineuser_group_condition` (
  `id` bigint(20) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `query_column_key` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `query_operator` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `query_value` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineusergroupcondition` ;
CREATE TABLE `lineusergroupcondition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `query_column_key` varchar(30) DEFAULT NULL,
  `query_operator` varchar(10) DEFAULT NULL,
  `query_value` varchar(500) DEFAULT NULL,
  `query_logical_operator` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_group_filelist` ;
CREATE TABLE `lineuser_group_filelist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `original_filename` varchar(100) DEFAULT NULL,
  `filename` varchar(100) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `expression` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_link` ;
CREATE TABLE `lineuser_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `linked` varchar(1) COLLATE utf8_unicode_ci DEFAULT 'N' COMMENT 'Y: 綁定\nN: 未綁定',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_reward` ;
CREATE TABLE `lineuser_reward` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `lineuser_id` bigint(50) DEFAULT NULL COMMENT 'line用戶，對應至 lineuser 表中的 id',
  `reward_card_id` bigint(50) DEFAULT NULL COMMENT '集點卡，對應至 reward_card 的 id',
  `reward_card_point` int(11) DEFAULT NULL COMMENT '目前此張集點卡可用點數',
  `reward_card_point_used` int(11) DEFAULT NULL COMMENT '已使用的點數總和',
  `event_id` bigint(50) DEFAULT NULL COMMENT '活動id，對應 event 的 id ',
  `latest_get_point_time` datetime DEFAULT NULL COMMENT '最近取得點數的時間',
  `status` int(11) DEFAULT NULL COMMENT '狀態，0：未使用、1：已使用',
  `valid_begin_time` datetime DEFAULT NULL COMMENT '有效開始，有效時間（開始時間）',
  `valid_end_time` datetime DEFAULT NULL COMMENT '有效結束，有效期間（結束時間）',
  `valie_end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_reward_point` ;
CREATE TABLE `lineuser_reward_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `lineuser_reward_id` bigint(20) NOT NULL COMMENT 'Line uid，對應至 lineuser 的 id ',
  `reward_card_point` int(11) DEFAULT '0' COMMENT '集點卡點數',
  `point_source` varchar(20) DEFAULT NULL COMMENT '點數來源，目前是 invoice',
  `point_source_id` bigint(20) DEFAULT NULL COMMENT '對應 point_source 的表的 id ',
  `point_operation` varchar(10) DEFAULT NULL COMMENT '點數運算，add：增加、sub：減少',
  `create_time` datetime DEFAULT NULL COMMENT '建立時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_reward_exchange` ;
CREATE TABLE `lineuser_reward_exchange` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `lineuser_id` bigint(20) DEFAULT NULL COMMENT '對應到 lineuser 表的 id',
  `lineuser_reward_point_id` bigint(20) DEFAULT NULL COMMENT '對應到 lineuser_reward_point 的 id',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `gender` varchar(255) DEFAULT NULL COMMENT '性別',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `tel_phone` varchar(255) DEFAULT NULL COMMENT '行動電話',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `email` varchar(255) DEFAULT NULL COMMENT '電子信箱',
  `child_count` int(11) DEFAULT NULL COMMENT '小孩數量',
  `constellation` varchar(255) DEFAULT NULL COMMENT '星座',
  `favorite_online_store` varchar(255) DEFAULT NULL COMMENT '喜歡的線上商家',
  `favorite_real_store` varchar(255) DEFAULT NULL COMMENT '喜歡的實體商家',
  `follow_type` varchar(255) DEFAULT NULL COMMENT '有在關心的主題',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_reward_exchange_detail` ;
CREATE TABLE `lineuser_reward_exchange_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reward_card_prize_id` bigint(20) DEFAULT NULL COMMENT '對應到 reward_card_prize 的 id',
  `lineuser_reward_exchange_id` bigint(20) DEFAULT NULL COMMENT '對應到 lineuser_reward_exchange 的 id',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `reward_card_point` int(11) DEFAULT NULL COMMENT '花費的點數',
  `amount` int(11) DEFAULT NULL COMMENT '兌換的數量',
  `memo` varchar(255) DEFAULT NULL COMMENT '備註（例如尺寸）',
  `create_time` datetime DEFAULT NULL COMMENT '創建時間',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lineuser_track` ;
CREATE TABLE `lineuser_track` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `source` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'UNLINKED/ BLOCK/ NORMALLY/ LINKED',
  `creation_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `linkaddress` ;
CREATE TABLE `linkaddress` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `linkaddress_id_seq` ;
CREATE TABLE `linkaddress_id_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into linkaddress_id_seq (next_val) values (1);

DROP TABLE IF EXISTS `linkaddress_link_address_list` ;
CREATE TABLE `linkaddress_link_address_list` (
  `link_address_id` bigint(20) NOT NULL,
  `link_address_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_jg7rkg26uwg8u89c5ihfl2tvq` (`link_address_list_id`),
  KEY `FKge6rdfjqa6d6pg1n840kj7jb1` (`link_address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `linkaddresslist` ;
CREATE TABLE `linkaddresslist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `link_id` bigint(20) DEFAULT NULL,
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `tag` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `linkid_idx` (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `linkaddress_tag` ;
CREATE TABLE `linkaddress_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link_id` bigint(20) DEFAULT NULL,
  `tag` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lineid_idx` (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `linkaddress_track` ;
CREATE TABLE `linkaddress_track` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lineuser_id` bigint(20) DEFAULT NULL,
  `linkaddress_id` bigint(20) DEFAULT NULL,
  `linkaddresslist_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `linkaddress_track_id_seq` ;
CREATE TABLE `linkaddress_track_id_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into linkaddress_track_id_seq (next_val) values (1);

DROP TABLE IF EXISTS `lottery_log` ;
CREATE TABLE `lottery_log` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(50) DEFAULT NULL,
  `type` char(1) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `filter` varchar(400) DEFAULT NULL,
  `expected_amount` int(10) DEFAULT NULL,
  `actual_amount` int(10) DEFAULT NULL,
  `memo` varchar(2000) DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `eventid_idx2` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_audio` ;
CREATE TABLE `message_audio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `original_content_url` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `duration` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_carousel_action` ;
CREATE TABLE `message_carousel_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `column_id` int(11) DEFAULT NULL,
  `template_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'MESSAGE' COMMENT 'POSTBACK / MESSAGE / URI',
  `label` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `data` varchar(300) COLLATE utf8_unicode_ci DEFAULT '',
  `text` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `uri` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `columnid_idx` (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_carousel_column` ;
CREATE TABLE `message_carousel_column` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `carousel_id` int(11) NOT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'column',
  `thumbnail_image_url` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `image_background_color` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `text` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `carouselid_idx` (`carousel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_carousel_template` ;
CREATE TABLE `message_carousel_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'carousel',
  `alt_text` varchar(400) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `image_aspect_ratio` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'rectangle' COMMENT 'rectangle / square',
  `image_size` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'cover' COMMENT 'cover / contain',
  `modify_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_datetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_image` ;
CREATE TABLE `message_image` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `original_content_url` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `preview_image_url` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_imagemap` ;
CREATE TABLE `message_imagemap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `base_url` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `alt_text` varchar(400) COLLATE utf8_unicode_ci NOT NULL,
  `base_size_width` int(4) NOT NULL,
  `base_size_height` int(4) NOT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'IMAGEMAP',
  `modify_time` datetime DEFAULT NULL,
  `modify_account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_imagemap_action` ;
CREATE TABLE `message_imagemap_action` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `message_id` int(10) unsigned NOT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'uri/message',
  `label` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `link_uri` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `text` varchar(400) COLLATE utf8_unicode_ci DEFAULT NULL,
  `area_x` int(4) NOT NULL,
  `area_y` int(4) NOT NULL,
  `area_width` int(4) NOT NULL,
  `area_height` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `messageid_idx` (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_imagemap_message_imagemap_action` ;
CREATE TABLE `message_imagemap_message_imagemap_action` (
  `message_image_map_id` int(11) NOT NULL,
  `message_imagemap_action_id` int(11) NOT NULL,
  UNIQUE KEY `messageimagemapactionid_idx` (`message_imagemap_action_id`),
  KEY `messageimagemapid_idx` (`message_image_map_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_imagemap_tag` ;
CREATE TABLE `message_imagemap_tag` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `message_id` int(11) unsigned DEFAULT NULL,
  `tag` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_msg_imagmap_tag_id_idx` (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_location` ;
CREATE TABLE `message_location` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `address` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `latitude` double NOT NULL DEFAULT '0',
  `longitude` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_sticker` ;
CREATE TABLE `message_sticker` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sticker_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `package_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_template` ;
CREATE TABLE `message_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `alt_text` varchar(400) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `thumbnail_image_url` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `image_aspect_ratio` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'rectangle' COMMENT 'rectangle / square',
  `image_size` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'cover' COMMENT 'cover / contain',
  `image_background_color` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `text` varchar(2000) COLLATE utf8_unicode_ci NOT NULL,
  `modify_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_datetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_template_action` ;
CREATE TABLE `message_template_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` int(11) DEFAULT NULL,
  `template_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'MESSAGE' COMMENT 'POSTBACK / MESSAGE / URI',
  `label` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `data` varchar(300) COLLATE utf8_unicode_ci DEFAULT '',
  `text` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `uri` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `templateid_idx` (`template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_text` ;
CREATE TABLE `message_text` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message_video` ;
CREATE TABLE `message_video` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `original_content_url` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `preview_image_url` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `otp_detail` ;
CREATE TABLE `otp_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `expire_time` datetime DEFAULT NULL,
  `otp_code` int(11) DEFAULT NULL,
  `send_status` varchar(255) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `product` ;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '產品名稱',
  `image_url` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '產品圖檔\n',
  `thumbnail_image_url` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '產品小圖',
  `description` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',
  `instruction` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '使用說明',
  `instruction_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `memo` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT ' 說明',
  `memo_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `querycolumn` ;
CREATE TABLE `querycolumn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `column_key` bigint(20) DEFAULT NULL,
  `column_name` varchar(30) DEFAULT NULL,
  `column_format` varchar(30) DEFAULT NULL,
  `column_status` int(11) DEFAULT NULL,
  `column_index` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `queryoperator` ;
CREATE TABLE `queryoperator` (
  `id` bigint(20) NOT NULL,
  `attr_key` varchar(50) DEFAULT NULL,
  `operator` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `questionnaire_main` ;
CREATE TABLE `questionnaire_main` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `subject` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '主題',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `modify_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人員',
  `modify_datetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `questionnaire` ;
CREATE TABLE `questionnaire` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `questionnaire_main_id` bigint(50) DEFAULT NULL COMMENT '對應到 questionnaire_main 表的 id',
  `question_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '問題',
  `question_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '問題類別：\nTEXT\nRADIO\nCHECKBOX\nGROUP',
  `required` int(11) DEFAULT NULL COMMENT '是否為必填欄位，0為非必填、1為必填',
  `order_index` int(11) DEFAULT NULL COMMENT '順序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `question` ;
CREATE TABLE `question` (
  `id` bigint(50) NOT NULL COMMENT '自動增長列',
  `questionnaire_id` bigint(50) DEFAULT NULL COMMENT '對應到 questionnaire 的 id',
  `parent_id` bigint(50) DEFAULT NULL COMMENT '父識別碼，對應到 question 表的 id',
  `item` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '問答選項',
  `item_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '項目類型\n主要為Table問答用 column 欄 row 列',
  `order_index` int(11) DEFAULT NULL COMMENT '順序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `questionnaire_answer`;
CREATE TABLE `questionnaire_answer` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `questionnaire_main_id` bigint(50) DEFAULT NULL COMMENT '對應到 questionnaire_main 表的 id',
  `questionnaire_id` bigint(50) DEFAULT NULL COMMENT '對應到 questionnaire 表的 id',
  `question_id` bigint(50) DEFAULT NULL COMMENT '對應到 question 表的 id',
  `lineuser_id` bigint(50) DEFAULT NULL COMMENT '對應到 lineuser 表的 id',
  `user_filled` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '回答內容',
  `modify_datetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `retailer` ;
CREATE TABLE `retailer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `modify_datetime` datetime DEFAULT NULL,
  `modify_user` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `retailer_detail` ;
CREATE TABLE `retailer_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `retailer_id` bigint(20) NOT NULL,
  `company_id` varchar(10) NOT NULL,
  `company_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `retailerid_idx` (`retailer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `reward_card` ;
CREATE TABLE `reward_card` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `reward_point` int(11) DEFAULT NULL COMMENT '點數，此集點卡最大點數量',
  `name` varchar(50) DEFAULT NULL COMMENT '名稱',
  `title` varchar(50) DEFAULT NULL COMMENT '標題，集點卡標題',
  `sub_title` varchar(50) DEFAULT NULL COMMENT '副標題，集點卡副標題',
  `background_image_url` varchar(100) DEFAULT NULL COMMENT '底圖',
  `thumbnail_url` varchar(100) DEFAULT NULL COMMENT '小圖',
  `valid_begin_time` datetime DEFAULT NULL COMMENT '有效開始，有效時間（開始時間）',
  `valid_end_time` datetime DEFAULT NULL COMMENT '有效結束，有效時間（結束時間）',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `instruction` varchar(200) DEFAULT NULL COMMENT '使用說明',
  `memo` varchar(5000) DEFAULT NULL COMMENT '說明',
  `reward_card_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `reward_card_prize` ;
CREATE TABLE `reward_card_prize` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長值',
  `reward_card_id` bigint(50) DEFAULT NULL COMMENT '集點卡 id，對應到 reward_card 的 id',
  `prize_id` bigint(50) DEFAULT NULL COMMENT '獎品序號，依據 prize_type 對應至不同的資料表，現在是 product_id',
  `prize_type` varchar(10) DEFAULT NULL COMMENT '贈品對應的資料表，目前只有 product',
  `announce_datetime` datetime DEFAULT NULL COMMENT '開始日期，贈品可使用開始日',
  `expired_datetime` datetime DEFAULT NULL COMMENT '到期日，贈品到期日',
  `volume` int(11) DEFAULT NULL COMMENT '數量，贈品數量',
  `collect_point` int(11) DEFAULT NULL COMMENT '設定多少點兌換點數',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `reward_card_prize_attribute` ;
CREATE TABLE `reward_card_prize_attribute` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `reward_card_prize_id` bigint(50) DEFAULT NULL COMMENT '集點卡贈品 id，對應至 reward_card_prize 的 id ',
  `attribute_group_name` varchar(50) DEFAULT NULL COMMENT '屬性名稱',
  `attribute_group_key` varchar(20) DEFAULT NULL COMMENT '屬性 key ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `reward_card_prize_attribute_value` ;
CREATE TABLE `reward_card_prize_attribute_value` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `reward_card_prize_attribute_id` bigint(50) DEFAULT NULL COMMENT '集點卡屬性贈品 id，對應至 reward_card_prize_attribute 的 id',
  `attribute_name` varchar(50) DEFAULT NULL COMMENT '屬性名稱',
  `attribute_key` varchar(20) DEFAULT NULL COMMENT '屬性 key',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message` ;
CREATE TABLE `send_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mode` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'ONCE' COMMENT 'ONCE/EVERYDAY/MONTHLY/WEEKLY',
  `mode_week` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'SUN, MON, TUE, WED, THU, FRI, SAT',
  `mode_day` int(2) DEFAULT NULL COMMENT '0-31',
  `mode_hour` int(2) DEFAULT NULL COMMENT '0-23',
  `mode_min` int(2) DEFAULT NULL COMMENT '0-59',
  `group_id` bigint(20) DEFAULT NULL,
  `schedule` datetime DEFAULT NULL,
  `category` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '訊息類別(TAG)',
  `lineuser_count` int(11) DEFAULT '0',
  `create_account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lineuser_ok_count` int(11) DEFAULT '0',
  `status` int(1) DEFAULT '0' COMMENT '0　新建\n-1 草稿',
  `creation_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modify_account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_list` ;
CREATE TABLE `send_message_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `send_id` bigint(20) DEFAULT NULL,
  `message_id` int(11) NOT NULL,
  `message_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `order_index` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sendid_idx` (`send_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_list_message_audio_list` ;
CREATE TABLE `send_message_list_message_audio_list` (
  `send_message_list_id` bigint(20) NOT NULL,
  `message_audio_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messageaudiolistid_idx1` (`message_audio_list_id`),
  KEY `sendmessagelistid_idx` (`send_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_list_message_image_list` ;
CREATE TABLE `send_message_list_message_image_list` (
  `send_message_list_id` bigint(20) NOT NULL,
  `message_image_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messageimagelistid_idx1` (`message_image_list_id`),
  KEY `sendmessagelistid_idx1` (`send_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_list_message_image_map_list` ;
CREATE TABLE `send_message_list_message_image_map_list` (
  `send_message_list_id` bigint(20) NOT NULL,
  `message_image_map_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messageimagemaplistid_idx1` (`message_image_map_list_id`),
  KEY `sendmessagelistid_idx2` (`send_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_list_message_sticker_list` ;
CREATE TABLE `send_message_list_message_sticker_list` (
  `send_message_list_id` bigint(20) NOT NULL,
  `message_sticker_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messagestickerlistid_idx1` (`message_sticker_list_id`),
  KEY `sendmessagelistid_idx3` (`send_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_list_message_template_list` ;
CREATE TABLE `send_message_list_message_template_list` (
  `send_message_list_id` bigint(20) NOT NULL,
  `message_template_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messagetemplatelistid_idx1` (`message_template_list_id`),
  KEY `sendmessagelistid_idx4` (`send_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_list_message_text_list` ;
CREATE TABLE `send_message_list_message_text_list` (
  `send_message_list_id` bigint(20) NOT NULL,
  `message_text_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messagetextlistid_idx1` (`message_text_list_id`),
  KEY `sendmessagelistid_idx5` (`send_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_list_message_video_list` ;
CREATE TABLE `send_message_list_message_video_list` (
  `send_message_list_id` bigint(20) NOT NULL,
  `message_video_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `messagevideolistid_idx1` (`message_video_list_id`),
  KEY `sendmessagelistid_idx6` (`send_message_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_tag` ;
CREATE TABLE `send_message_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `send_message_id` bigint(20) DEFAULT NULL,
  `tag` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sendmessageid_idx` (`send_message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_users` ;
CREATE TABLE `send_message_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `send_id` bigint(20) NOT NULL,
  `line_user_id` bigint(20) NOT NULL,
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '0:建立\n8:失敗\n9:成功',
  `response_code` int(3) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `status_idx` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_users_backup` ;
CREATE TABLE `send_message_users_backup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `send_id` bigint(20) NOT NULL,
  `line_user_id` bigint(20) NOT NULL,
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '0:建立\n8:失敗\n9:成功',
  `response_code` int(3) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `send_message_users_push_job` ;
CREATE TABLE `send_message_users_push_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `send_id` bigint(20) NOT NULL,
  `index_value` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `share_record` ;
CREATE TABLE `share_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `lineuser_id` bigint(20) DEFAULT NULL,
  `share_type` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分享類別\nevent 活動\nmgm ',
  `share_source_id` bigint(20) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `share_record_track` ;
CREATE TABLE `share_record_track` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `share_id` bigint(20) unsigned DEFAULT NULL,
  `lineuser_id` bigint(20) unsigned DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名稱',
  `storecode` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Storecode\n店家代碼',
  `branch` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分行',
  `city` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '城市',
  `manager` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '店經理',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `telephone` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地址',
  `security_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `beacon_uuid` varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `beacon_marjor` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `beacon_minor` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `low_stock_volume` int(11) DEFAULT '0' COMMENT '低庫存警示量\n0: 為不警示',
  `low_stock_alert` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'Y' COMMENT '低庫存警示通知\n\nY:通知\nN:不通知',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '備註\n',
  `modify_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `store_id_seq`;
CREATE TABLE `store_id_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into store_id_seq (next_val) values (1); 

DROP TABLE IF EXISTS `store_group`;
CREATE TABLE `store_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '群組名稱',
  `modify_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='店家群組';

DROP TABLE IF EXISTS `store_group_detail`;
CREATE TABLE `store_group_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `store_group_id` int(11) unsigned DEFAULT NULL,
  `store_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `storeid_idx2` (`store_id`),
  KEY `storegroupid_idx` (`store_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='店家群組明細';

DROP TABLE IF EXISTS `store_product`;
CREATE TABLE `store_product` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `store_id` bigint(50) DEFAULT NULL COMMENT '對應至 store 表的 id ',
  `product_id` bigint(50) DEFAULT NULL COMMENT '對應至 product 表的 id',
  `current_volume` int(11) DEFAULT NULL COMMENT '現有數量',
  `low_stock_volume` int(11) DEFAULT NULL COMMENT '低庫存警示量，0 為不警示',
  `modify_user` varchar(50) DEFAULT NULL COMMENT '修改人員',
  `modify_datetime` datetime DEFAULT NULL COMMENT '修改時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `store_product_detail`;
CREATE TABLE `store_product_detail` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '自動增長列',
  `store_product_id` bigint(50) DEFAULT NULL COMMENT '對應至 store_product 表的 id',
  `compute_volume` int(11) DEFAULT NULL COMMENT '異動的數量',
  `compute_event` varchar(50) DEFAULT NULL COMMENT '紀錄哪個活動使用的',
  `compute_event_source` varchar(50) DEFAULT NULL COMMENT '紀錄活動的來源',
  `compute_event_id` bigint(50) DEFAULT NULL COMMENT '對應到 event 表的 id',
  `modify_user` varchar(50) DEFAULT NULL COMMENT '修改人員',
  `modify_datetime` datetime DEFAULT NULL COMMENT '修改時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `system_column_config`;
CREATE TABLE `system_column_config` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(50) DEFAULT NULL,
  `column_name` varchar(50) DEFAULT NULL,
  `column_value` varchar(50) DEFAULT NULL,
  `column_value_description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `config_value` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `template_id_seq` ;
CREATE TABLE `template_id_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 insert into template_id_seq (next_val) values (1); 

DROP TABLE IF EXISTS `track_tag` ;
CREATE TABLE `track_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `track_id` bigint(20) DEFAULT NULL,
  `track_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'MESSAGE\nIMAGEMAP\nLINKADDRESS',
  `tag` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '註記 類別',
  `track_count` int(11) DEFAULT NULL COMMENT '點擊次數',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `track_tag_detail` ;
CREATE TABLE `track_tag_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `track_tag_id` int(11) DEFAULT NULL COMMENT '對應 track_tag.id',
  `lineuser_id` bigint(20) DEFAULT NULL COMMENT '對應 Lineuser.id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `upload_uid`;
CREATE TABLE `upload_uid` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `filename` varchar(100) CHARACTER SET utf8 NOT NULL,
  `original_filename` varchar(100) CHARACTER SET utf8 NOT NULL,
  `uid` varchar(33) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `uploaduids`;
CREATE TABLE `uploaduids` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filepath` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `uid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_click`;
CREATE TABLE `user_click` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lineuser_uid` varchar(33) COLLATE utf8_unicode_ci NOT NULL,
  `lineuser_status` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `subtype` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mapping_id` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ------------------------------------------------------------------------------
-- 權限設定 

DROP TABLE IF EXISTS `systemuser`;
CREATE TABLE `systemuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `password` char(70) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `status` int(11) DEFAULT NULL,
  `lineuser_uid` char(33) DEFAULT NULL,
  `login_status` int(11) DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_account` varchar(20) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `modify_account` varchar(20) DEFAULT NULL,
  `system_flag` char(1) DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
INSERT INTO `systemuser` (`id`,`account`,`name`,`password`,`status`,`lineuser_uid`,`login_status`,`login_time`,`create_time`,`create_account`,`modify_time`,`modify_account`,`system_flag`) VALUES (1,'admin','admin','$2a$10$6LXHXB/p75/c5q/.AZQW/.6RDBKGfIte.OBNwZohOYe2mgODgbU0y',1,NULL,NULL,NULL,'2018-10-01 00:00:00','admin','2018-10-01 00:00:00','admin','N');

DROP TABLE IF EXISTS `systemuserrole`;
CREATE TABLE `systemuserrole` (
  `id` char(10) NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_idx` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `systemuserrole` (`id`,`name`) VALUES ('Role000001','admin');
INSERT INTO `systemuserrole` (`id`,`name`) VALUES ('Role000002','editor');
INSERT INTO `systemuserrole` (`id`,`name`) VALUES ('Role000003','marketing staff');

DROP TABLE IF EXISTS `systemuserrole_function`;
CREATE TABLE `systemuserrole_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `systemuserrole_id` char(10) DEFAULT NULL,
  `function_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000001','1');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000001','2');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000001','3');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000001','4');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000001','5');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000002','1');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000002','2');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000002','3');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000002','5');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000003','1');
INSERT INTO `systemuserrole_function` (`systemuserrole_id`,`function_id`) VALUES ('Role000003','2');

DROP TABLE IF EXISTS `userroles`;
CREATE TABLE `userroles` (
  `user_id` int(11) NOT NULL,
  `role_id` char(10) NOT NULL,
  KEY `roleid_idx` (`role_id`),
  KEY `userid_idx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `userroles` (`user_id`,`role_id`) VALUES (1,'Role000001');
