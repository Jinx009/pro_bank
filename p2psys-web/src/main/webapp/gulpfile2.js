var gulp = require('gulp'),
    connect = require('gulp-connect'), //- 创建服务器
    sass = require('gulp-ruby-sass'), //- sass处理
    autoprefixer = require('gulp-autoprefixer'), //- 添加兼容前缀
    rev = require('gulp-rev'), //- 对文件名加MD5后缀
    revCollector = require('gulp-rev-collector'); //- 路径替换

//服务器配置
gulp.task('web', function() {
    connect.server({
        port: 8888,
        livereload: true
    });
});

//刷新浏览器
gulp.task('livereload', function() {
    gulp.src('*')
        .pipe(connect.reload());
});

//scss预处理（解析，autoprefix，md5）
gulp.task('styles', function() {
    sass('themes/theme_default/nb/wechat/sass/account/coupons.scss') //- 需要处理的scss文件，放到一个字符串里
        .pipe(autoprefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1', 'ios 6', 'android 4')) //- 添加兼容性前缀
        .pipe(rev()) //- 文件名添加md5后缀
        .pipe(gulp.dest('themes/theme_default/nb/wechat/css/account/')) //- 处理得到的css文件发布到css目录
        .pipe(rev.manifest('coupons.rev-manifest.json', {
            merge: true  // 如果rev-manifest.json文件已存在，合并
        })) //- 生成一个rev-manifest.json文件
        .pipe(gulp.dest('themes/theme_default/nb/wechat/rev/account/')) //- 将 rev-manifest.json 保存到 rev 目录内
        .pipe(connect.reload()); //- 刷新浏览器
});

//md5文件路径替换
gulp.task('rev', function() {
    gulp.src(['themes/theme_default/nb/wechat/rev/account/coupons.rev-manifest.json', 'WEB-INF/html_default/nb/wechat/account/coupons.html']) //- 读取 rev-manifest.json 文件以及需要进行css，js名替换的文件
        .pipe(revCollector()) //- 执行文件内css、js名的替换
        .pipe(gulp.dest('WEB-INF/html_default/nb/wechat/account/')) //- 替换后的文件输出的目录
})

//监听文件变化，处理scss，刷新浏览器
gulp.task('watch', function() {
    gulp.watch('themes/theme_default/nb/wechat/sass/account/coupons.scss', ['styles', 'rev']);
    gulp.watch('*', ['livereload']);
});

//设置默认任务
gulp.task('default', ['styles', 'rev', 'web', 'watch']);
