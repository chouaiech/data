const path = require('path');

module.exports = {
  dev: {
    env: require('./dev.env'),
    port: 8080,
    autoOpenBrowser: true,
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {},
    cssSourceMap: false
  },
  test: {
    env: require('./prod.env'),
    port: 8080,
    index: path.resolve(__dirname, '../dist/index.html'),
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    productionSourceMap: true,
    productionCompression: false,
    productionBrotliExtensions: ['js', 'css', 'html', 'svg'],
    productionGzipExtensions: ['js', 'css', 'html', 'svg'],
    bundleAnalyzerReport: process.env.npm_config_report
  },
  build: {
    env: require('./prod.env'),
    port: 8080,
    index: path.resolve(__dirname, '../dist/index.html'),
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/data/',
    productionSourceMap: true,
    productionCompression: false,
    productionBrotliExtensions: ['js', 'css', 'html', 'svg'],
    productionGzipExtensions: ['js', 'css', 'html', 'svg'],
    bundleAnalyzerReport: process.env.npm_config_report
  }
};
