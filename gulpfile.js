var gulp = require('gulp'),
    autoprefixer = require('gulp-autoprefixer'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename'),
    notify = require('gulp-notify');

gulp.task('parser', function () {
    return gulp.src('js/parser.js')
        .pipe(uglify())
        .pipe(rename('parser.min.js'))
        .pipe(gulp.dest('dist/'));
});

gulp.task('build', ['parser']);

gulp.task('default', ['build']);