--新增旧知识升级表，在oms库执行 ZhaoJie@HF 2014-7-14
DROP TABLE IF EXISTS t_km_old_knowledge_upgrade;
CREATE TABLE t_km_old_knowledge_upgrade (
old_knowledge_upgrade_id VARCHAR (32) NOT NULL PRIMARY KEY,
corp_code varchar (50) NOT NULL ,
corp_name varchar (50) NOT NULL ,
before_online_status VARCHAR (20) DEFAULT 'not_start' NOT NULL,
after_online_status VARCHAR (20) DEFAULT 'not_start' NOT NULL,
old_latest_knowledge_id VARCHAR (32),
CONSTRAINT  "u_km_old_knowledge_upgrade_cc" UNIQUE (corp_code)
)

WITH (OIDS=FALSE)
;

ALTER TABLE "t_km_old_knowledge_upgrade" OWNER TO "postgres";

COMMENT ON TABLE "t_km_old_knowledge_upgrade" IS '旧知识升级表';
COMMENT ON COLUMN "t_km_old_knowledge_upgrade"."old_knowledge_upgrade_id" IS  '主键';
COMMENT ON COLUMN "t_km_old_knowledge_upgrade"."corp_code" IS  '公司编码';
COMMENT ON COLUMN "t_km_old_knowledge_upgrade"."corp_name" IS  '公司名称';
COMMENT ON COLUMN "t_km_old_knowledge_upgrade"."before_online_status" IS
'上线前旧知识状态，用于判断旧知识增加索引。not_start：未开始，execute：执行中，success：成功，failed：失败。';
COMMENT ON COLUMN "t_km_old_knowledge_upgrade"."after_online_status" IS
'上线后旧知识状态，用于判断旧知识更新索引。not_start：未开始，execute：执行中，success：成功，failed：失败。';
COMMENT ON COLUMN "t_km_old_knowledge_upgrade"."old_latest_knowledge_id" IS '知识上线前升级时，最新上传的知识id';

INSERT INTO t_km_old_knowledge_upgrade SELECT corp_id,corp_code,corp_name FROM t_oms_corp
WHERE account_status!='REGISTERED_CORP' AND end_time>LOCALTIMESTAMP;


--新增知识展现升级png状态表，在oms库执行 ZhaoJie@HF 2014-8-12
DROP TABLE IF EXISTS t_km_knowledge_upgrade_png;
CREATE TABLE t_km_knowledge_upgrade_png (
knowledge_id VARCHAR (32) NOT NULL PRIMARY KEY,
stored_file_id VARCHAR (50) NOT NULL UNIQUE ,
corp_code VARCHAR (50) NOT NULL,
status VARCHAR (20) DEFAULT 'executing' NOT NULL
)
WITH (OIDS=FALSE)
;

ALTER TABLE "t_km_knowledge_upgrade_png" OWNER TO "postgres";
COMMENT ON TABLE "t_km_knowledge_upgrade_png" IS '知识展现升级png状态表';
COMMENT ON COLUMN "t_km_knowledge_upgrade_png"."knowledge_id" IS  '主键';
COMMENT ON COLUMN "t_km_knowledge_upgrade_png"."stored_file_id" IS  '知识存储id';
COMMENT ON COLUMN "t_km_knowledge_upgrade_png"."corp_code" IS  '知识所属的公司';
COMMENT ON COLUMN "t_km_knowledge_upgrade_png"."status" IS
'知识展现升级png状态,包括执行中executing，失败failed，成功success，文件不找到not_exist';