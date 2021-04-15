/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 100137
Source Host           : localhost:3306
Source Database       : library_manager

Target Server Type    : MYSQL
Target Server Version : 100137
File Encoding         : 65001

Date: 2021-04-15 10:29:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_menu`;
CREATE TABLE `admin_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `path` varchar(64) DEFAULT NULL COMMENT '路径',
  `name` varchar(64) DEFAULT NULL COMMENT '菜单名',
  `name_zh` varchar(64) DEFAULT NULL COMMENT '菜单名（中文）',
  `icon_cls` varchar(64) DEFAULT NULL COMMENT '图标',
  `component` varchar(64) DEFAULT NULL COMMENT '路由',
  `parent_id` int(11) DEFAULT NULL COMMENT '父级id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of admin_menu
-- ----------------------------
INSERT INTO `admin_menu` VALUES ('1', '/admin', 'AdminIndex', '首页', 'el-icon-s-home', 'AdminIndex', '0');
INSERT INTO `admin_menu` VALUES ('2', '/admin/dashboard', 'DashboardAdmin', '运行情况', null, 'dashboard/admin/index', '1');
INSERT INTO `admin_menu` VALUES ('3', '/admin', 'User', '用户管理', 'el-icon-user', 'AdminIndex', '0');
INSERT INTO `admin_menu` VALUES ('4', '/admin', 'Content', '图书管理', 'el-icon-reading', 'AdminIndex', '0');
INSERT INTO `admin_menu` VALUES ('5', '/admin', 'System', '系统配置', 'el-icon-s-tools', 'AdminIndex', '0');
INSERT INTO `admin_menu` VALUES ('6', '/admin/user/profile', 'Profile', '用户信息', null, 'user/UserProfile', '3');
INSERT INTO `admin_menu` VALUES ('7', '/admin/user/role', 'Role', '角色配置', null, 'user/Role', '3');
INSERT INTO `admin_menu` VALUES ('8', '/admin/content/book', 'BookManagement', '图书管理', null, 'content/BookManagement', '4');
INSERT INTO `admin_menu` VALUES ('9', '/admin/content/banner', 'BannerManagement', '广告管理', null, 'content/BannerManagement', '4');
INSERT INTO `admin_menu` VALUES ('10', '/admin/content/article', 'ArticleManagement', '文章管理', null, 'content/ArticleManagement', '4');
INSERT INTO `admin_menu` VALUES ('18', '/admin/borrow/borrow', 'BorrowManagement', '借阅管理', '', 'borrow/BorrowManagement', '24');
INSERT INTO `admin_menu` VALUES ('19', '/admin/borrow/message', 'MessageManagement', '留言板', null, 'borrow/MessageManagement', '24');
INSERT INTO `admin_menu` VALUES ('20', '/admin/borrow/book', 'StudentBookManagement', '借阅图书', null, 'borrow/StudentBookManagement', '24');
INSERT INTO `admin_menu` VALUES ('22', '/admin/borrow/student', 'StudentBorrowManagement', '借阅信息', '', 'borrow/StudentBorrowManagement', '24');
INSERT INTO `admin_menu` VALUES ('23', '/admin/borrow/message', 'ContentMessageManagement', '反馈管理', '', 'borrow/ContentMessageManagement', '24');
INSERT INTO `admin_menu` VALUES ('24', '/admin', 'Borrow', '借阅管理', 'el-icon-s-cooperation', 'AdminIndex', '0');

-- ----------------------------
-- Table structure for admin_permission
-- ----------------------------
DROP TABLE IF EXISTS `admin_permission`;
CREATE TABLE `admin_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '权限名',
  `desc_` varchar(100) DEFAULT NULL COMMENT '中文描述',
  `url` varchar(100) DEFAULT NULL COMMENT '链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of admin_permission
-- ----------------------------
INSERT INTO `admin_permission` VALUES ('1', 'users_management', '用户管理', '/api/admin/user');
INSERT INTO `admin_permission` VALUES ('2', 'roles_management', '角色管理', '/api/admin/role');
INSERT INTO `admin_permission` VALUES ('3', 'content_management', '内容管理', '/api/admin/content');
INSERT INTO `admin_permission` VALUES ('4', 'message_management', '留言管理', '/api/admin/message');
INSERT INTO `admin_permission` VALUES ('5', 'borrow_menagement', '借阅管理', '/api/admin/borrow');

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '角色名',
  `name_zh` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `enabled` tinyint(1) DEFAULT NULL COMMENT '是否可用（0不可 1可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', 'sysAdmin', '系统管理员', '1');
INSERT INTO `admin_role` VALUES ('2', 'contentManager', '内容管理员', '1');
INSERT INTO `admin_role` VALUES ('3', 'visitor', '访客', '0');
INSERT INTO `admin_role` VALUES ('9', 'test', '测试角色', '0');
INSERT INTO `admin_role` VALUES ('10', 'student', '学生', '1');

-- ----------------------------
-- Table structure for admin_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_menu`;
CREATE TABLE `admin_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` int(11) DEFAULT NULL COMMENT '角色id',
  `mid` int(11) DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=utf8 COMMENT='角色菜单表';

-- ----------------------------
-- Records of admin_role_menu
-- ----------------------------
INSERT INTO `admin_role_menu` VALUES ('19', '4', '1');
INSERT INTO `admin_role_menu` VALUES ('20', '4', '2');
INSERT INTO `admin_role_menu` VALUES ('21', '3', '1');
INSERT INTO `admin_role_menu` VALUES ('22', '3', '2');
INSERT INTO `admin_role_menu` VALUES ('23', '9', '1');
INSERT INTO `admin_role_menu` VALUES ('24', '9', '2');
INSERT INTO `admin_role_menu` VALUES ('77', '2', '1');
INSERT INTO `admin_role_menu` VALUES ('78', '2', '2');
INSERT INTO `admin_role_menu` VALUES ('79', '2', '4');
INSERT INTO `admin_role_menu` VALUES ('80', '2', '8');
INSERT INTO `admin_role_menu` VALUES ('81', '2', '9');
INSERT INTO `admin_role_menu` VALUES ('82', '2', '10');
INSERT INTO `admin_role_menu` VALUES ('121', '1', '1');
INSERT INTO `admin_role_menu` VALUES ('122', '1', '2');
INSERT INTO `admin_role_menu` VALUES ('123', '1', '3');
INSERT INTO `admin_role_menu` VALUES ('124', '1', '4');
INSERT INTO `admin_role_menu` VALUES ('125', '1', '6');
INSERT INTO `admin_role_menu` VALUES ('126', '1', '7');
INSERT INTO `admin_role_menu` VALUES ('127', '1', '8');
INSERT INTO `admin_role_menu` VALUES ('128', '1', '9');
INSERT INTO `admin_role_menu` VALUES ('129', '1', '10');
INSERT INTO `admin_role_menu` VALUES ('194', '1', '18');
INSERT INTO `admin_role_menu` VALUES ('222', '1', '23');
INSERT INTO `admin_role_menu` VALUES ('223', '1', '24');
INSERT INTO `admin_role_menu` VALUES ('224', '10', '1');
INSERT INTO `admin_role_menu` VALUES ('225', '10', '2');
INSERT INTO `admin_role_menu` VALUES ('226', '10', '19');
INSERT INTO `admin_role_menu` VALUES ('227', '10', '20');
INSERT INTO `admin_role_menu` VALUES ('228', '10', '22');
INSERT INTO `admin_role_menu` VALUES ('232', '10', '24');

-- ----------------------------
-- Table structure for admin_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_permission`;
CREATE TABLE `admin_role_permission` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `rid` int(20) DEFAULT NULL COMMENT '角色id',
  `pid` int(20) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`),
  KEY `fk_role_permission_role_1` (`rid`),
  KEY `fk_role_permission_permission_1` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
-- Records of admin_role_permission
-- ----------------------------
INSERT INTO `admin_role_permission` VALUES ('83', '9', '3');
INSERT INTO `admin_role_permission` VALUES ('97', '2', '3');
INSERT INTO `admin_role_permission` VALUES ('108', '1', '1');
INSERT INTO `admin_role_permission` VALUES ('109', '1', '2');
INSERT INTO `admin_role_permission` VALUES ('110', '1', '3');
INSERT INTO `admin_role_permission` VALUES ('140', '3', '1');
INSERT INTO `admin_role_permission` VALUES ('141', '10', '3');
INSERT INTO `admin_role_permission` VALUES ('142', '1', '4');
INSERT INTO `admin_role_permission` VALUES ('143', '1', '5');
INSERT INTO `admin_role_permission` VALUES ('169', '11', '3');
INSERT INTO `admin_role_permission` VALUES ('170', '11', '4');
INSERT INTO `admin_role_permission` VALUES ('171', '11', '5');

-- ----------------------------
-- Table structure for admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL COMMENT '用户id',
  `rid` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  KEY `fk_operator_role_operator_1` (`uid`),
  KEY `fk_operator_role_role_1` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of admin_user_role
-- ----------------------------
INSERT INTO `admin_user_role` VALUES ('40', '24', '2');
INSERT INTO `admin_user_role` VALUES ('63', '3', '2');
INSERT INTO `admin_user_role` VALUES ('64', '1', '1');
INSERT INTO `admin_user_role` VALUES ('65', '2', '3');
INSERT INTO `admin_user_role` VALUES ('67', '4', '10');

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `cover` varchar(255) DEFAULT '' COMMENT '封面图片',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '书名',
  `author` varchar(255) DEFAULT '' COMMENT '作者',
  `date` varchar(20) DEFAULT '' COMMENT '出版日期',
  `press` varchar(255) DEFAULT '' COMMENT '出版社',
  `abs` varchar(255) DEFAULT NULL COMMENT '简介',
  `cid` int(11) DEFAULT NULL COMMENT '类型id',
  `stock` int(11) unsigned DEFAULT '0' COMMENT '在库数量',
  `sum` int(11) unsigned DEFAULT '0' COMMENT '总数量',
  PRIMARY KEY (`id`),
  KEY `fk_book_category_on_cid` (`cid`),
  CONSTRAINT `fk_book_category_on_cid` FOREIGN KEY (`cid`) REFERENCES `category` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='书籍详情表';

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('1', 'https://i.loli.net/2019/04/10/5cadaa0d0759b.jpg', '且在人间', '余秀华', '2019-2-1', '湖南文艺出版社', '诗人余秀华中篇小说首次结集出版。\r\n\r\n        《且在人间》——以余秀华为生活原型，讲述一个残疾女人悲苦倔强、向死而生的故事。\r\n\r\n        女主人公周玉生活在乡村，患有“脑瘫”，她几乎被所有人漠视，甚至被整个社会抛弃，但是她渴望被当成一个普通的健康人,而不是带着怜悯或不屑，她只要求平等。爱情的缺 失，家庭的不幸，生活的种种际遇让周玉用诗歌的方式把 情感抒发出来，最终她用诗歌创作出了一个文学的世界，得到了人们的认可。', '2', '5', '5');
INSERT INTO `book` VALUES ('2', 'https://i.loli.net/2019/04/10/5cada7e73d601.jpg', '三体', '刘慈欣', ' 2008-1', '重庆出版社', '文化大革命如火如荼进行的同时。军方探寻外星文明的绝秘计划“红岸工程”取得了突破性进展。但在按下发射键的那一刻，历经劫难的叶文洁没有意识到，她彻底改变了人类的命运。地球文明向宇宙发出的第一声啼鸣，以太阳为中心，以光速向宇宙深处飞驰……\r\n\r\n四光年外，“三体文明”正苦苦挣扎——三颗无规则运行的太阳主导下的百余次毁灭与重生逼迫他们逃离母星。而恰在此时。他们接收到了地球发来的信息。在运用超技术锁死地球人的基础科学之后。三体人庞大的宇宙舰队开始向地球进发……\r\n\r\n人类的末日悄然来临。', '2', '5', '5');
INSERT INTO `book` VALUES ('32', 'https://i.loli.net/2019/04/10/5cada99bd8ca5.jpg', '叙事的虚构性', '[美] 海登·怀特 ', '2019-3', '南京大学出版社', '海登•怀特被誉为人类伟大的思想家之一。从1973年出版具有里程碑意义的专著《元史学》以来，怀特的作品对于历史学、文学研究、人类学、哲学、艺术史、电影传媒研究等将叙事学作为关注焦点的学科而言意义非凡。\n\n本书由罗伯特•多兰作序，他巧妙地将怀特重要但难得一见的文章汇集成册，研究探讨他关于历史书写和叙事的革命性理论。怀特的这些文章大多采用论文体，内容涉及多位思想家，探讨诸多主题，文笔犀利，语言优美。\n\n《叙事的虚构性》追溯怀特重要思想的演变轨迹，是历史编纂学者和学习者、历史理论和文学研究学者们的重要读物。', '3', '5', '5');
INSERT INTO `book` VALUES ('35', 'https://i.loli.net/2019/04/10/5cada940e206a.jpg', '圣母', '[日]秋吉理香子 ', '2019-3', '新星出版社', '一起男童被害案搅得蓝出市人心惶惶。\n\n好不容易怀孕生产的保奈美抱紧年幼的孩子，立誓要不惜任何代价保护她。\n\n男人是在孩子出生后才成为父亲的，但女人，是从小生命来到体内的那一瞬间起，就是母亲了。患有不孕症的保奈美是经历过艰辛的治疗过程才终于有了孩子的，她不允许这起命案威胁到宝贵的孩子！\n\n母亲，就是要消除所有对子女的威胁，每一位母亲都应肩负这样的使命，这是神圣的天职！', '1', '5', '5');
INSERT INTO `book` VALUES ('37', 'https://i.loli.net/2019/04/10/5cada8986e13a.jpg', '奢侈与逸乐', '[英]马克辛·伯格', '2019-3', '中国工人出版社', '本书探讨了十八世纪英国新式、时尚的消费品的发明、制造和购买。', '3', '5', '5');
INSERT INTO `book` VALUES ('38', 'https://i.loli.net/2019/04/10/5cada8b8a3a17.jpg', '忧伤动物', '[德]莫妮卡·马龙 ', '2019-4', '漓江出版社', '“忧伤动物”(animal triste)这个词组取自一句最早可以追溯到亚里士多德时代的拉丁语名言，即“欢爱后，每个动物都忧伤不已”（Post coitum omne animal triste est）。无疑，这部冠以如此标题的小说让人有不祥的预感并暗示着宿命的思想。小说的女主人公是位近乎百岁的老人。在多年前有意斩断了与外界的一切联系之后，在她的后半生里，她唯一能做的就是或躺或坐在“印着鲜红、艳绿和深紫色的大花”、让人想起“食肉植物的花朵”的床单上，追忆几十年前她和自己...', '1', '5', '5');
INSERT INTO `book` VALUES ('54', 'https://i.loli.net/2019/04/10/5cada9d9d23a6.jpg', '爱界', '[英] 费伊·韦尔登 ', '2019-3-1', '人民文学出版社', '去不去爱，爱的界限何在，一直是普拉克西丝的人生课题。\n\n年迈的她独自待在肮脏而昏暗的地下室里，想写回忆录，可她该写些什么呢？是写父母未婚同居生下了她，她还年幼天真无邪时，母女就遭父亲抛弃？还是写她曾经或是主动或是被动地成了未婚同居者、妻子、情人、母亲、后母？还是写她两年的牢狱生活？她想描绘二十世纪女性的众生相，想记录女性群体在情感、灵魂和思想方面所处的三重困境，想道出女性之间的大爱如何铸成姐妹之谊。', '3', '5', '5');
INSERT INTO `book` VALUES ('63', 'https://i.loli.net/2019/04/10/5cada962c287c.jpg', '与病对话', '胡冰霜', '2019-3-31', '北京联合出版公司', '一部融合科普性与趣味性、兼具心理学与哲学意味的医学散文。\n\n一位满怀仁心的资深医者对几十年行医生涯的回望与省思。\n\n全书以真实的病例和鲜活的故事贯穿始终，作者从一位全科医生、心理学者的视角观察、解读疾病与患者身心之关系，厘清大众对诸多常见疾病的误解...', '1', '5', '5');
INSERT INTO `book` VALUES ('64', 'https://i.loli.net/2019/04/10/5cada858e6019.jpg', '上帝笑了99次', '[英]彼得·凯弗', '2019-2', '北京联合出版公司', '一只美洲羊驼会坠入爱河吗？机器人能变成人吗？怎样才能不赢得公主青睐？人类一思考，上帝就发笑。在99个奇妙、怪诞、滑稽的问题背后，其实是99个烧脑的哲学、道德、法律领域的经典悖论，也是99道极富挑战性的大思考测试。本书内容覆盖了大多数常见哲学话题，包括形而上学、逻辑学、伦理学、语言哲学、政治哲学、自我认知、人际关系、美学、存在主义等，还配有20多幅漫画插图。在锻炼思维之外，本书也能帮我们建立个性化的哲学知识体系。', '3', '5', '5');
INSERT INTO `book` VALUES ('65', 'https://i.loli.net/2019/04/10/5cada8e1aa892.jpg', '互联网算法', '[美] 菲斯曼等 ', '2019-4', '江西人民出版社', '只要你租过房子、上网买过东西、自己经营过企业，那么你就处在商业变革的前线。在这场变革中，亚马逊、谷歌、优步等不同以往的企业取得了史无前例的成功，而促成这场变革的不只是科技进步，还有经济学思想。\n\n在这本趣味横生的书中，我们会看到，经济思想的革命远比科技革命更宏大。从谷歌广告的算法，到网上购物规避欺诈，都要依靠经济学家建立的经济模型，甚至连互联网公司...', '6', '5', '5');
INSERT INTO `book` VALUES ('66', 'https://i.loli.net/2019/04/10/5cada9ec514c9.jpg', '七侯笔录', '马伯庸', '2019-4-15', '湖南文艺出版社', '一个关于文化的离奇故事，一段关于文人的壮丽传说。\n\n几千年来，每一位风华绝代的文人墨客辞世之时，都会让自己的灵魂寄寓在一管毛笔之中。他们身躯虽去，才华永存，这些伟大的精神凝为性情不一的笔灵，深藏于世间，只为一句“不教天下才情付诸东流”的誓言。其中最伟大的七位古人，他们所凝聚的七管笔灵，被称为“管城七侯”。\n\n一位不学无术的现代少年，无意中邂逅了李白的青莲笔，命运就此与千年之前的诗仙交织一处，并为他开启了一个叫作笔冢的神秘世界。', '3', '5', '5');
INSERT INTO `book` VALUES ('67', 'https://i.loli.net/2019/04/10/5cada9870c2ab.jpg', '中心与边缘', '[美] 希尔斯', '2019-3', '译林出版社', '美国著名社会学家爱德华·希尔斯的主要研究成果包括他对“克里斯玛”、“中心”和“边缘”等概念的解释，以及他对“大众社会”一词的修正，这些研究对分析政治和文化领导力以及社会凝聚力具有重要价值。本书对希尔斯数十载社会理论研究进行了全面而详细的总结，为解释与探究当代社会的结构与变化提供了极具科学性的参考依据。', '3', '5', '5');
INSERT INTO `book` VALUES ('68', 'https://i.loli.net/2019/04/10/5cad643643d4c.jpg', '水浒群星闪耀时', '李黎', '2019-4', '上海文艺出版社', '本书以众所周知的梁山英雄为写作对象，重点书写其上山后、招安前的日常生活，涉及他们的喜怒哀乐、同类中人、乡愁怀旧、未来憧憬、命运追问等。书中涉及宋江、武松、鲁智深、李俊、燕青等等耳熟能详的人物而显得有些“改编”与水浒研究的意味，但鉴于所有人物皆为虚构，本书稿的内容是虚构之上的虚构，旨在宏大叙事的语境下突出个人的细微之处和命运感。', '1', '5', '5');
INSERT INTO `book` VALUES ('69', 'https://i.loli.net/2019/04/10/5cad63931ce27.jpg', '谋杀狄更斯', '[美] 丹·西蒙斯 ', '2019-4', '上海文艺出版社', '“狄更斯的那场意外灾难发生在1865年6月9日，那列搭载他的成功、平静、理智、手稿与情妇的火车一路飞驰，迎向铁道上的裂隙，突然触目惊心地坠落了。”', '1', '5', '5');
INSERT INTO `book` VALUES ('70', 'http://localhost:8443/api/file/p2.jpg', '像艺术家一样思考', '[英] 威尔·贡培兹', '2019-4', '湖南美术出版社', '归纳成就艺术大师的 10 个关键词\n\n揭示大师们的创作秘辛\n\n凝聚 BBC 艺术频道主编威尔·贡培兹职业生涯的所见、所知、所想\n\n·\n\n威尔·贡培兹是你能遇到的最好的老师\n\n——《卫报》', '3', '5', '5');

-- ----------------------------
-- Table structure for borrow
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `bookid` int(10) unsigned NOT NULL COMMENT '书籍id',
  `bookname` varchar(255) DEFAULT NULL COMMENT '书名',
  `type` varchar(255) DEFAULT NULL COMMENT '借阅类型',
  `borrow_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
  `return_time` timestamp NULL DEFAULT NULL COMMENT '还书时间',
  `status` varchar(255) DEFAULT NULL COMMENT '状态（0已还书 1未还书 2已预约 3预约超时）',
  `money` int(255) unsigned DEFAULT '0' COMMENT '罚款',
  `mail` varchar(255) DEFAULT NULL COMMENT '预约邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='图书借阅表';

-- ----------------------------
-- Records of borrow
-- ----------------------------
INSERT INTO `borrow` VALUES ('4', 'student', '70', '像艺术家一样思考', '0', '2021-01-01 11:14:44', '2021-03-30 18:17:24', '0', '0', null);
INSERT INTO `borrow` VALUES ('5', 'admin', '70', '像艺术家一样思考', '0', '2021-03-30 11:17:54', '2021-03-30 15:12:39', '0', '0', null);
INSERT INTO `borrow` VALUES ('6', 'admin', '70', '像艺术家一样思考', '1', '2021-03-30 14:56:40', '2021-03-30 15:11:32', '0', '0', null);
INSERT INTO `borrow` VALUES ('7', 'admin', '70', '像艺术家一样思考', '0', '2021-04-01 16:32:43', '2021-04-01 16:41:35', '0', '0', null);
INSERT INTO `borrow` VALUES ('8', 'admin', '70', '像艺术家一样思考', '0', '2021-04-01 16:44:29', '2021-04-08 15:52:09', '0', '0', null);
INSERT INTO `borrow` VALUES ('9', 'student', '69', '谋杀狄更斯', '1', '2021-03-13 16:55:23', null, '3', '0', null);
INSERT INTO `borrow` VALUES ('10', 'admin', '70', '像艺术家一样思考', '1', '2021-04-08 15:52:36', '2021-04-08 16:03:50', '0', '0', '2803647229@qq.com');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='书籍类型表';

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', '文学');
INSERT INTO `category` VALUES ('2', '流行');
INSERT INTO `category` VALUES ('3', '文化');
INSERT INTO `category` VALUES ('4', '生活');
INSERT INTO `category` VALUES ('5', '经管');
INSERT INTO `category` VALUES ('6', '科技');

-- ----------------------------
-- Table structure for jotter_article
-- ----------------------------
DROP TABLE IF EXISTS `jotter_article`;
CREATE TABLE `jotter_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_title` varchar(255) DEFAULT NULL COMMENT '文章标题',
  `article_content_html` longtext COMMENT '文章内容',
  `article_content_md` longtext COMMENT '文章内容',
  `article_abstract` varchar(255) DEFAULT NULL COMMENT '文章简介',
  `article_cover` varchar(255) DEFAULT NULL COMMENT '封面',
  `article_date` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- ----------------------------
-- Records of jotter_article
-- ----------------------------
INSERT INTO `jotter_article` VALUES ('1', '凉风有兴', '凉风有兴，秋月无边，而我思乡的情绪好比度日如年。虽然我风流倜傥玉树临风，但我还是有聪明的头脑和强健的臂腕。', '凉风有兴，秋月无边，而我思乡的情绪好比度日如年。虽然我风流倜傥玉树临风，但我还是有聪明的头脑和强健的臂腕。', '凉风有兴，秋月无边，而我思乡的情绪好比度日如年。', 'https://i.loli.net/2020/01/16/d2ZlKI1WRE4p7XB.png', '2020-01-13 21:14:27');
INSERT INTO `jotter_article` VALUES ('2', '爱你一万年', '<p>曾经有份真挚的爱情摆在我的面前，我没有珍惜，等到失去的时候才后悔莫急，人世间最痛苦的事莫过余此，如果上天在给我一次机会，我会对那个女孩说我爱你，如果要在这份爱上加个期限，我希望是一万年。</p>\n', '曾经有份真挚的爱情摆在我的面前，我没有珍惜，等到失去的时候才后悔莫急，人世间最痛苦的事莫过余此，如果上天在给我一次机会，我会对那个女孩说我爱你，如果要在这份爱上加个期限，我希望是一万年。', '曾经有份真挚的爱情摆在我的面前，我没有珍惜，等到失去的时候才后悔莫及，人世间最痛苦的事莫过于此。', 'https://i.loli.net/2020/01/16/DdGBk1R3mj5er6v.png', '2020-01-16 00:00:00');

-- ----------------------------
-- Table structure for message_board
-- ----------------------------
DROP TABLE IF EXISTS `message_board`;
CREATE TABLE `message_board` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `message` longtext COMMENT '留言内容',
  `username` varchar(255) DEFAULT NULL COMMENT '留言者',
  `type` varchar(255) DEFAULT NULL COMMENT '留言类型(1建议 2读后感)',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(1) DEFAULT '0' COMMENT '状态（0正常 1删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='留言榜';

-- ----------------------------
-- Records of message_board
-- ----------------------------
INSERT INTO `message_board` VALUES ('1', 'ssds', 'admin', '1', '2021-03-23 17:02:50', '0');
INSERT INTO `message_board` VALUES ('2', null, 'admin', null, '2021-03-29 11:45:36', '0');
INSERT INTO `message_board` VALUES ('3', 'dghmj', 'admin', '2', '2021-03-29 11:47:34', '0');
INSERT INTO `message_board` VALUES ('4', 'null', 'admin', '1', '2021-03-29 11:50:27', '0');
INSERT INTO `message_board` VALUES ('5', 'fdasg', 'admin', '2', '2021-03-29 11:54:54', '0');
INSERT INTO `message_board` VALUES ('6', 'null', 'admin', '1', '2021-03-29 11:56:41', '1');
INSERT INTO `message_board` VALUES ('7', 'gfm', 'admin', '1', '2021-03-29 11:58:39', '1');
INSERT INTO `message_board` VALUES ('8', 'abuddcuerfbuybfvauhvbdahsfgiueygfuqbvdhjcabfhgeht', 'admin', '2', '2021-03-29 14:01:59', '0');
INSERT INTO `message_board` VALUES ('9', '75/786786', 'admin', '1', '2021-03-29 17:00:23', '0');
INSERT INTO `message_board` VALUES ('10', 'sgwerhqhefh', 'admin', '2', '2021-03-30 16:33:30', '0');
INSERT INTO `message_board` VALUES ('11', '1763', 'admin', '2', '2021-03-31 11:41:14', '0');
INSERT INTO `message_board` VALUES ('12', 'shngnj', 'student', '2', '2021-04-01 16:54:58', '0');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `username` char(255) NOT NULL COMMENT '登录名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `salt` varchar(255) DEFAULT NULL COMMENT '盐',
  `name` varchar(255) DEFAULT NULL COMMENT '名字',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否可用',
  `level` int(2) DEFAULT '1' COMMENT '用户级别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '35b9529f89cfb9b848060ca576237e17', '8O+vDNr2sI3N82BI31fu1A==', '管理员', '12312312312', 'evan_nightly@163.com', '1', '1');
INSERT INTO `user` VALUES ('2', 'test', '85087738b6c1e1d212683bfafc163853', 'JBba3j5qRykIPJQYTNNH9A==', '测试', '12312312312', '123@123.com', '1', '1');
INSERT INTO `user` VALUES ('3', 'editor', '8583a2d965d6159edbf65c82d871fa3e', 'MZTe7Qwf9QgXBXrZzTIqJQ==', '编辑', null, null, '1', '1');
INSERT INTO `user` VALUES ('4', 'student', '85fd2c094504164473b5f0aa0acd34f7', 'CIH8GtzETv/QXoVtn46smA==', '123', '123', '123@qq.com', '1', '2');
