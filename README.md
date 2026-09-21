# 校园活动报名管理系统（本科毕业设计）

基于 **SpringBoot 2.7 + Vue 2 + ElementUI + MySQL 8** 的校园活动报名管理系统，全程本地运行，无需云服务器。

## 一、技术栈

| 层级 | 技术 | 版本 |
|---|---|---|
| 后端 | SpringBoot | 2.7.18 |
| 后端 | MyBatis-Plus | 3.5.3.2 |
| 后端 | JWT（jjwt） | 0.11.5 |
| 安全 | BCrypt 密码加密 | spring-security-crypto 5.7.11 |
| 前端 | Vue | 2.6.14 |
| 前端 | ElementUI | 2.15.14 |
| 前端 | Vue Router / Axios | 3.6.5 / 1.x |
| 数据库 | MySQL | 8.0.x（5.7 亦可） |

> 注意：前端使用 **Vue 2.6.14** 而非 2.7.x——ElementUI 的 el-table 与 Vue 2.7 存在兼容性问题（表格列无法渲染），2.6.14 为验证过的稳定组合。

## 二、目录结构

```
activity-system
├── sql/init.sql                  # 建库建表脚本（含预置数据）
├── backend/                      # SpringBoot 后端
│   ├── pom.xml
│   └── src/main/java/com/activity/
│       ├── common/               # 统一返回、异常、JWT、拦截器
│       ├── config/               # MyBatis-Plus / BCrypt Bean 配置
│       ├── controller/           # 接口层
│       ├── dto/                  # 请求参数
│       ├── entity/               # 实体类（4 张表）
│       ├── mapper/               # 数据访问层
│       ├── service/              # 业务层
│       └── vo/                   # 视图对象
└── frontend/                     # Vue2 前端
    ├── package.json
    ├── webpack.config.js         # 手写 webpack 5 配置（devServer 代理 /api → 8080）
    └── src/
        ├── router/               # 路由 + 登录/角色守卫
        ├── utils/request.js      # axios 封装（token 注入、401 处理）
        ├── api/                  # 接口定义
        └── views/                # 学生端 + 管理端页面
```

## 三、环境要求

- JDK 1.8（JAVA_HOME 已配置）
- Maven 3.6+（配置了阿里云镜像加速）
- Node.js 14~20（本项目验证于 Node 20）
- MySQL 8.0 本机服务已启动，root 密码：`123456`

## 四、启动步骤

### 1. 初始化数据库（首次）

```bash
mysql -uroot -p123456 --default-character-set=utf8mb4 -e "source D:/doubao/activity-system/sql/init.sql"
```

### 2. 启动后端（端口 8080）

```bash
cd D:\doubao\activity-system\backend
mvn spring-boot:run
```

启动成功标志：控制台输出 `Started ActivityApplication`，无报错。

### 3. 启动前端（端口 8081）

```bash
cd D:\doubao\activity-system\frontend
npm install        # 首次
npm run serve
```

浏览器访问 **http://localhost:8081**

### 4. 生产构建（可选）

```bash
cd D:\doubao\activity-system\frontend
npm run build      # 产物输出到 frontend/dist
```

## 五、VS Code 运行指南

**前提**：MySQL 服务已启动（`services.msc` 中 mysql 状态为"正在运行"），数据库已初始化。

1. VS Code 打开文件夹 `D:\doubao\activity-system`（整个项目根目录）；
2. 安装插件：`Extension Pack for Java`（Java 运行）、`Vetur`（Vue2 语法）、`Database Client`（可选，查库）；
3. 打开终端（Ctrl + `）启动后端：
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   或打开 `ActivityApplication.java` 点击 `main` 方法左侧 ▶ Run 按钮；
4. 点击终端面板 **+ 新建终端**，启动前端（后端终端保持运行）：
   ```bash
   cd frontend
   npm run serve
   ```
5. 浏览器访问 **http://localhost:8081**。

常见问题：
- `mvn 不是内部或外部命令`：Maven 环境变量配置后需**重启 VS Code** 才生效；
- 端口 8080/8081 被占用：说明已有服务在运行，先结束旧进程再启动；
- 后端启动报数据库连接失败：确认 MySQL 服务已启动、root 密码为 `123456`。

## 六、IntelliJ IDEA 配置指南

**前提**：JDK 1.8（`C:\Program Files\Java\jdk1.8.0_202`）、Maven（`D:\doubao\tools\apache-maven-3.9.16`）、MySQL 服务运行中。

1. `File → Open` 选择 `D:\doubao\activity-system\backend` 目录，首次打开点 **Load Maven Project** 等待依赖下载；
2. 配置 JDK：`File → Project Structure` → Project → SDK 选择 `jdk1.8.0_202`，Language level 选 **8**（Modules 里同样设为 8）；
3. 配置 Maven：`File → Settings → Build Tools → Maven` → Maven home path 填 `D:\doubao\tools\apache-maven-3.9.16`，User settings file 勾选 Override 填同目录 `conf\settings.xml`（含阿里云镜像）；
4. 运行后端：打开 `com.activity → ActivityApplication.java`，右键 → **Run 'ActivityApplication'**，控制台出现 `Started ActivityApplication` 即成功（端口 8080）；
5. 运行前端：点底部 `Terminal`（Alt+F12）执行 `cd D:\doubao\activity-system\frontend` 后 `npm run serve`，浏览器访问 **http://localhost:8081**。

常见问题：
- 端口 8080 被占用：当前有服务在运行，先结束旧进程再启动；
- 控制台中文乱码：`Settings → Editor → File Encodings` 全部设 UTF-8，运行配置 VM options 加 `-Dfile.encoding=UTF-8`；
- 依赖下载失败：确认第四步的 settings.xml 已生效（阿里云镜像）；
- 前端代码高亮：IDEA Community 无官方 Vue 插件，前端建议用 VS Code 编辑，IDEA 专管后端。

## 七、默认账号

| 角色 | 账号 | 密码 | 说明 |
|---|---|---|---|
| 管理员 | admin | 123456 | 预置账号，密码 BCrypt 加密存储 |
| 学生 | stu001 | 123456 | 测试账号，也可在注册页自主注册 |

## 八、功能清单

**通用**
- 注册 / 登录（JWT 会话保持）、个人中心（修改手机号、修改密码）

**学生端**
- 活动大厅：搜索、分类筛选、报名状态/名额展示
- 活动详情：完整信息 + 实时已报名人数/剩余名额
- 在线报名：重复报名、截止、满员、下架自动拦截
- 我的报名：查看记录、审核状态、取消未开始活动的报名

**管理端**
- 数据概览：学生数、活动数、报名数、进行中活动、待审核数、分类分布
- 用户管理：搜索、新增、禁用/启用、重置密码（默认 123456）
- 分类管理：增删改查（有活动的分类禁止删除）
- 活动管理：增删改查、发布/下架（级联删除报名记录）
- 报名审核：多条件筛选、通过/驳回（可填备注）

## 九、数据库设计（4 张核心表）

| 表名 | 说明 |
|---|---|
| sys_user | 用户表（学生 + 管理员），密码 BCrypt 存储 |
| activity_category | 活动分类表 |
| activity_info | 活动信息表（时间、地点、人数上限、发布状态） |
| activity_signup | 报名表（活动+用户唯一约束防重复报名，审核状态） |

设计规范：所有时间字段 `datetime`（北京时间）；表名字段统一下划线命名；
MySQL 连接串携带 `serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8`。

## 十、验收对照（已实测通过）

- [x] 前后端一键启动，控制台无报错
- [x] 学生注册登录正常，数据库无明文密码（BCrypt 加密）
- [x] 学生完整流程：浏览活动 → 报名 → 我的报名 → 取消报名 → 记录查看
- [x] 管理员完整流程：用户/分类/活动/报名增删改查
- [x] 时间显示正常，无时区错乱
- [x] 权限隔离：学生访问后台接口返回 403，未登录返回 401
