const webpack = require('webpack');
const { defineConfig } = require('@vue/cli-service');
const config = require('./config');

let buildMode;
if (process.env.NODE_ENV === 'production') {
  buildMode = process.env.BUILD_MODE === 'test' ? 'test' : 'build';
} else {
  buildMode = 'dev';
}

const buildConfig = {
  BASE_PATH: config[buildMode].assetsPublicPath,
  SERVICE_URL: config[buildMode].serviceUrl,
};

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: buildConfig.BASE_PATH,
  configureWebpack: {
    devtool: process.env.NODE_ENV === 'production' ? 'source-map' : 'eval-source-map',
    plugins: [
      new webpack.DefinePlugin({
        'process.env.buildconf': JSON.stringify(buildConfig)
      }),
    ]
  },

  // pluginOptions: {
  //   webpackBundleAnalyzer: {
  //     openAnalyzer: true,
  //   },
  // },

  chainWebpack: (config) => {
    // Preserve whitespaces between element tags if it contains new lines.
    // This fixes minor visual layout differences.
    // See https://github.com/vuejs/vue/tree/dev/packages/vue-template-compiler#options
    config
      .module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options.compilerOptions.whitespace = 'preserve';
        return options;
      }
    );

    // replace lodash with lodash-es
    // warning: this is a silent replacement
    config.resolve.alias.set('lodash', 'lodash-es');

    // Use source-map-loader
    config
      .module
      .rule('source-map')
      .test(/\.js$/)
      .enforce('pre')
      .use('source-map-loader')
      .loader('source-map-loader')
      .end();
  }
});
