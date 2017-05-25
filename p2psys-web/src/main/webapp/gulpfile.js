var gulp = require('gulp'),
    connect = require('gulp-connect'), //- 创建服务器
    px2rem = require('gulp-px3rem'), //- px转换rem
    sass = require('gulp-sass'), //- sass处理
    autoprefixer = require('gulp-autoprefixer'), //- 添加兼容前缀
    cssnano = require('gulp-cssnano'),  //-压缩css
    sourcemaps = require('gulp-sourcemaps');  //-添加map文件

//服务器配置
gulp.task('web', function() {
    connect.server({
        port: 8890,
        livereload: true
    });
});

//刷新浏览器
gulp.task('livereload', function() {
    gulp.src('*')
        .pipe(connect.reload());
});

//scss预处理（解析，autoprefix）
gulp.task('styles', function() {
    gulp.src('themes/theme_default/nb/wechat/sass/survey/riskSurvey.scss')  //- 需要处理的scss文件，放到一个字符串里
        .pipe(sourcemaps.init())  //- map初始化
            .pipe(sass().on('error', sass.logError))  //- 把scss文件编译成css文件
            .pipe(autoprefixer()) //- 添加兼容性前缀
            .pipe(px2rem())
            .pipe(cssnano())  //-压缩css
        .pipe(sourcemaps.write('./maps'))  //- map另存
        .pipe(gulp.dest('themes/theme_default/nb/wechat/css/survey/')) //- 处理得到的css文件发布到css目录
});

//监听文件变化，处理scss，刷新浏览器
gulp.task('watch', function() {
    gulp.watch('themes/theme_default/nb/wechat/sass/survey/riskSurvey.scss', ['styles','livereload']);
    gulp.watch('*', ['livereload']);
    gulp.watch('WEB-INF/html_default/nb/pc/survey/riskSurvey.html',['livereload']);
    gulp.watch('WEB-INF/html_default/nb/wechat/survey/riskSurvey.html',['livereload']);
});

//设置默认任务
// gulp.task('default', ['styles', 'web', 'watch']);

//设置自动刷新
gulp.task('autoload', ['web', 'watch']);
