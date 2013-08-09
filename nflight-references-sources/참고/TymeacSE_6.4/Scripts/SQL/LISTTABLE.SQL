create table LIST_TABLE (
FUNC_NAME varchar(80) not null,
SEQ_NBR int not null,
QUEUE_NAME varchar(80)
);

ALTER TABLE LIST_TABLE
   ADD CONSTRAINT LISTTABLE_PK Primary Key (
      FUNC_NAME, SEQ_NBR);