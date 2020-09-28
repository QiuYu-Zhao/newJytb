#2017-02-22 在 报警表， 预警表中添加 电量字段   

ALTER TABLE `t_monkeyking_report_log` ADD COLUMN battery  INT(4) COMMENT '电量' AFTER voltage;
ALTER TABLE `t_monkeyking_warning_log` ADD COLUMN battery INT(4) COMMENT '电量' AFTER voltage