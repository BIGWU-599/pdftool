# PDF Tool - 智能PDF处理工具

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

一个功能强大的Java PDF处理工具，基于Apache PDFBox构建，提供PDF解析、转换、表格提取等功能，并支持桌面应用和Web视图集成。

## 📋 目录

- [特性](#特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [使用说明](#使用说明)
- [开发指南](#开发指南)
- [贡献](#贡献)
- [许可证](#许可证)

## ✨ 特性

### 核心功能
- 🔍 **PDF解析**: 高性能PDF文本和图像提取
- 📊 **表格识别**: 智能PDF表格检测和提取
- 🔄 **格式转换**: PDF转HTML、PDF合并/拆分
- 🏷️ **关键词提取**: 基于中文分词的关键词分析
- 📝 **数据抽取**: 结构化数据从PDF中提取

### 高级功能
- 🖥️ **桌面应用**: 基于JCEF的跨平台GUI界面
- 🌐 **Web视图**: 内置浏览器集成
- 📑 **模板引擎**: Thymeleaf模板支持
- 📈 **Excel导出**: 数据导出为Excel格式
- 🔎 **全文搜索**: PDF内容全文检索

## 🛠️ 技术栈

### 核心技术
- **Java 17** - 主要编程语言
- **Apache PDFBox 2.0.26** - PDF处理库
- **Maven** - 项目构建和依赖管理
- **Spring Boot 2.5.8** - 应用框架

### 关键依赖
- **pdf2dom** - PDF转HTML转换
- **Jcseg** - 中文分词引擎
- **Apache POI 4.1.2** - Excel文件处理
- **Thymeleaf 3.0.14** - 模板引擎
- **Jsoup 1.14.3** - HTML解析
- **Jackson** - JSON处理
- **Log4j2 2.17.2** - 日志框架

### UI技术
- **JCEF (Java Chromium Embedded Framework)** - 嵌入式浏览器
- **Swing** - 桌面GUI框架

## 📁 项目结构

```
pdftool/
├── core/                    # 核心模块 - PDF处理引擎
│   ├── src/main/java/
│   │   └── com/sfccn/pdftool/
│   │       ├── parse/      # PDF解析器
│   │       ├── utils/      # 工具类
│   │       ├── bean/       # 数据模型
│   │       └── main/       # 主程序入口
│   └── src/main/resources/ # 配置文件和模板
│
├── client/                  # 桌面客户端模块
│   ├── src/main/java/
│   │   └── com/sfccn/pdftool/
│   │       ├── client/     # 客户端核心
│   │       ├── ui/         # UI组件
│   │       └── webview/    # Web视图集成
│   └── libs/               # 本地库文件
│
├── client_keyword/          # 关键词分析模块
│   ├── src/main/java/
│   │   └── com/sfccn/pdftool/
│   │       ├── client/     # 关键词客户端
│   │       ├── view/       # 视图组件
│   │       └── utils/      # 工具类
│   └── src/main/resources/ # 资源文件
│
├── data/thymeleaf/          # Thymeleaf模板
├── jcef-bundle/            # JCEF运行时库
└── pom.xml                 # Maven父POM
```

## 🚀 快速开始

### 前置要求

- JDK 17 或更高版本
- Maven 3.6+
- Git

### 安装步骤

1. **克隆仓库**
```bash
git clone https://github.com/BIGWU-599/pdftool.git
cd pdftool
```

2. **编译项目**
```bash
mvn clean install
```

3. **运行核心模块**
```bash
cd core
mvn exec:java -Dexec.mainClass="com.sfccn.pdftool.main.Pdf2Html"
```

4. **启动桌面应用**
```bash
cd client
mvn exec:java -Dexec.mainClass="com.sfccn.pdftool.client.DesktopApp"
```

### IDE配置

推荐使用 **IntelliJ IDEA**:
1. File → Open → 选择项目根目录
2. 等待Maven依赖下载完成
3. 运行相应的Main类

## 📖 使用说明

### PDF转HTML

```java
// 示例代码
PdfDom2Html converter = new PdfDom2Html();
converter.convert("input.pdf", "output.html");
```

### 表格提取

```java
// 提取PDF中的表格数据
PdfTableMain tableExtractor = new PdfTableMain();
tableExtractor.extractTables("document.pdf");
```

### 关键词分析

```java
// 中文关键词提取
JcsegToken token = new JcsegToken();
List<String> keywords = token.extractKeywords(text);
```

### 桌面应用功能

启动桌面应用后，您可以：
- 打开PDF文件进行查看和分析
- 使用内置浏览器访问Web内容
- 执行关键词提取和分析
- 导出数据为Excel格式
- 使用脚本自动化处理

## 💻 开发指南

### 添加新的PDF解析器

1. 在 `core/src/main/java/com/sfccn/pdftool/parse/` 创建新类
2. 实现 `ParseValue` 接口
3. 注册解析器到主程序

### 构建可执行JAR

```bash
mvn clean package
```

生成的JAR文件位于各模块的 `target/` 目录

### 运行测试

```bash
mvn test
```

### 代码规范

- 遵循Java命名约定
- 使用UTF-8编码
- 编写单元测试
- 添加适当的日志记录

## 🤝 贡献

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 贡献准则

- 确保代码通过所有测试
- 添加必要的文档和注释
- 遵循现有的代码风格
- 更新README（如需要）

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👥 作者

- **BIGWU-599** - 初始工作 - [BIGWU-599](https://github.com/BIGWU-599)

## 🙏 致谢

- [Apache PDFBox](https://pdfbox.apache.org/) - PDF处理库
- [Jcseg](https://gitee.com/lionsoul/jcseg) - 中文分词引擎
- [JCEF](https://bitbucket.org/chromiumembedded/java-cef) - Java Chromium嵌入框架

---

⭐ 如果这个项目对您有帮助，请给个Star！
