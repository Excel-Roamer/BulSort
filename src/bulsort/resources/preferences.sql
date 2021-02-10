BEGIN TRANSACTION;

-- Table preferences
CREATE TABLE IF NOT EXISTS `preferences` ( 
    `id` INTEGER NOT NULL PRIMARY KEY,
    `libelle` VARCHAR ( 30 ),
    `value` TEXT
);
-- Content:
INSERT INTO `preferences` VALUES 
    (1, "PDF_DIR", ""),
    (2, "XL_DIR", ""),
    (3, "DEST_DIR", ""),
    (4, "DUPLICATE_PAGES", "0"),
    (5, "DEL_WATERMARK", "Y"),
    (6, "LANGUAGE", "FR"),
    (7, "PREPEND_TITLE_PAGE", "Y"),
    (8, "APPEND_TITLE_PAGE", "N"),
    (9, "OPEN_DEST_FOLDER", "Y"),
    (10, "MERGE_BULLETINS", "N"),
    (11, "DB_DIR", ""),
    (12, "RENAME_BULLETINS_IN_FILES", "N"),
    (13, "RENAME_PVS_IN_FILES", "N"),
    (14, "DATE_FORMAT_OUTPUT", "dd.MMM.yy-HH.mm.ss"),
    (15, "STAMP", ""),
    (16, "STAMP_DIR", ""),
    (17, "STAMP_X", "280"),
    (18, "STAMP_Y", "40"),
    (19, "USE_STAMP", "N"),
    (20, "STAMP_H", "100"),
    (21, "STAMP_W", "100"),
    (22, "SORT_BULLETINS", "Y"),
    (23, "0_TO_5_MALE", "ضاعف مجهوداتك"),
    (24, "5_TO_8_MALE", "عمل غير كاف.. اعمل أكثر"),
    (25, "8_TO_10_MALE", "دون المتوسط.. اعمل أكثر"),
    (26, "10_TO_12_MALE", "عمل متوسط.. مزيدا من المثابرة"),
    (27, "12_TO_14_MALE", "عمل لا بأس به"),
    (28, "14_TO_16_MALE", "عمل مشجع، واصل"),
    (29, "16_TO_18_MALE", "عمل جيد، وفقك الله"),
    (30, "18_TO_20_MALE", "عمل ممتاز، استمر وفقك الله"),
    (31, "0_TO_5_FEMALE", "ضاعفي مجهوداتك"),
    (32, "5_TO_8_FEMALE", "عمل غير كاف.. اعملي أكثر"),
    (33, "8_TO_10_FEMALE", "دون المتوسط.. اعملي أكثر"),
    (34, "10_TO_12_FEMALE", "عمل متوسط.. مزيدا من المثابرة"),
    (35, "12_TO_14_FEMALE", "عمل لا بأس به"),
    (36, "14_TO_16_FEMALE", "عمل مشجع، واصلي"),
    (37, "16_TO_18_FEMALE", "عمل جيد، وفقك الله"),
    (38, "18_TO_20_FEMALE", "عمل ممتاز، استمري وفقك الله"),
    (39, "DISPLAY_PVS_OBSERVATIONS", "N"),
    (40, "OBS_X", "40"),
    (41, "OBS_Y", "220"),
    (42, "USED_MARK", "0"),
    (43, "FONT_SIZE", "16")
;

COMMIT;