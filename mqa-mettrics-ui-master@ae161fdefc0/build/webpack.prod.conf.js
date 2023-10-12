'use strict'
const path = require('path')
const utils = require('./utils')
const config = require('../config')
const merge = require('webpack-merge')
const baseWebpackConfig = require('./webpack.base.conf')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const OptimizeCSSPlugin = require('optimize-css-assets-webpack-plugin')
const TerserPlugin = require('terser-webpack-plugin')
const PreloadWebpackPlugin = require('preload-webpack-plugin')

let buildMode
if (process.env.NODE_ENV === 'production') {
  buildMode = process.env.BUILD_MODE === 'test' ? process.env.BUILD_MODE : 'build'
} else {
  buildMode = 'dev'
}

const env = require(`../config/prod.env`)

const webpackConfig = merge(baseWebpackConfig, {
  mode: 'production',
  module: {
    rules: utils.styleLoaders({
      sourceMap: config[buildMode].productionSourceMap,
      extract: true
    })
  },
  devtool: config[buildMode].productionSourceMap ? config[buildMode].devtool : false,
  output: {
    path: config[buildMode].assetsRoot,
    publicPath: config[buildMode].assetsPublicPath,
    filename: utils.assetsPath("js/[name].[chunkhash].js"),
    chunkFilename: utils.assetsPath("js/[id].[chunkhash].js")
  },
  optimization: {
    nodeEnv: 'production',
    minimizer: [
      new TerserPlugin({
        sourceMap: true,
        parallel: true,
        cache: true
      })
    ],
    splitChunks: {
      chunks: 'all',
      name: true
    },
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: utils.assetsPath('css/[name].[hash].css'),
      chunkFilename: utils.assetsPath('css/[name].[hash].css'),
    }),
    new OptimizeCSSPlugin({
      cssProcessorOptions: config[buildMode].productionSourceMap
        ? { safe: true, map: { inline: false } }
        : { safe: true }
    }),
    new PreloadWebpackPlugin({
      rel: 'preload',
      include: 'allAssets'
    }),
    new CopyWebpackPlugin([
      {
        from: path.resolve(__dirname, "../static"),
        to: config[buildMode].assetsSubDirectory,
        ignore: [".*"]
      }
    ]),
  ]
})

if (config[buildMode].productionCompression) {
  const CompressionPlugin = require('compression-webpack-plugin')
  const zlib = require("zlib");

  webpackConfig.plugins.push(
    new CompressionPlugin({
      filename: "[path][base].br",
      algorithm: "brotliCompress",
      test: new RegExp(
        '\\.(' +
        config[buildMode].productionBrotliExtensions.join('|') +
        ')$'
      ),
      // test: /\.(js|css|html|svg)$/,
      compressionOptions: {
        params: {
          [zlib.constants.BROTLI_PARAM_QUALITY]: 11,
        },
      },
      threshold: 10240,
      minRatio: 0.8,
    })
  )

  webpackConfig.plugins.push(
    new CompressionPlugin({
      filename: "[path][base].gz",
      algorithm: "gzip",
      test: new RegExp(
        '\\.' +
        config[buildMode].productionGzipExtensions.join('$|\\.') +
        '$'
      ),
      // test: /\.js$|\.css$|\.html$|\.svg$/,
      threshold: 10240,
      minRatio: 0.8,
    })
  )
}

if (config[buildMode].bundleAnalyzerReport) {
  const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
  webpackConfig.plugins.push(new BundleAnalyzerPlugin())
}

module.exports = webpackConfig
