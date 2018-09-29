var ExtractTextPlugin = require('extract-text-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin');
var CleanObsoleteChunks = require('webpack-clean-obsolete-chunks');

var webpack = require('webpack');
var path = require('path');
var fs = require('fs');

module.exports = {
    // The standard entry point and output config
    entry: {
        common: './src/common/common'
        //, '3rdparty': './src/common/3rdparty'
        // Use extra files to induclde dependencies only used on some pages
        // A good exmaple would be a graphing library.
        //extras: './src/extras'
    },
    output: {
        path: __dirname + '/assets',
        filename: 'js/[name]-[hash].js',
        chunkFilename: '[id]-[hash].js',
        publicPath: '/assets/'
    },
    module: {
        loaders: [
            {
                test: /\.(otf|eot|ttf|woff)/,
                loader: 'file-loader?name=fonts/[name]-[hash].[ext]'
            }, {
                test: /\.(png|jpg|gif|ico|svg)/,
                loader: 'file-loader?name=images/[name].[ext]'
            }, {
                test: /\.css$/,
                loader: ExtractTextPlugin.extract({
                                            fallback: "style-loader",
                                            use: "css-loader",
                                          })
            }, {
                test: /\.scss$/,
                loader: ExtractTextPlugin.extract({
                                            fallback: "style-loader",
                                            use: "css-loader!sass-loader",
                                          })
            }, {
                test: /\.json$/,
                loader: 'json-loader'
            }, {
                test:    /\.js$/,
                // Some of the 3rd party libs are in ES6 so we need to run babel
                exclude: /(node_modules|bower_components)\//, //^shared
                loader:  'babel-loader',
                query:   {
                    presets: ['env']
                }
            }
        ]
    },

    // Use the plugin to specify the resulting filename (and add needed behavior to the compiler)
    plugins: [
        new CleanObsoleteChunks({deep: true}),
        new ExtractTextPlugin('css/[name]-[hash].css'),
        new webpack.optimize.UglifyJsPlugin({minimize: true}),
        // This was breaking some java script that expected certain names
        // Specifically the counter names for line-numbers
        new OptimizeCssAssetsPlugin({
          cssProcessorOptions: {
            reduceIdents: false
          },
        }),
        new CopyWebpackPlugin([
            {
                from: 'src/**/*.hbs',
                to: './templates/',
            }
        ]),
        new webpack.ProvidePlugin({
          // We shouldn't need all of these but ran into some issues
          // Adding all of them fixed it :(
          $: "jquery",
          jQuery: "jquery",
          'window.$': 'jquery',
          'window.jQuery': 'jquery',
          Popper: ['popper.js', 'default'],
          Util: "exports-loader?Util!bootstrap/js/dist/util",
          Dropdown: "exports-loader?Dropdown!bootstrap/js/dist/dropdown",
       }),
       // TODO: Fix this copied from https://github.com/webpack/webpack/issues/86#issuecomment-179957661
       function () {
            this.plugin("done", function (stats) {
                var replaceInFile = function (filePath, toReplace, replacement) {
                    var replacer = function (match) {
                        console.log('Replacing in %s: %s => %s', filePath, match, replacement);
                        return replacement
                    };
                    var str = fs.readFileSync(filePath, 'utf8');
                    var out = str.replace(new RegExp(toReplace, 'g'), replacer);
                    fs.writeFileSync(filePath, out);
                };

                var hash = stats.hash; // Build's hash, found in `stats` since build lifecycle is done.

                replaceInFile(
                  path.join(module.exports.output.path, 'templates/src/common/head.hbs'),
                  'common(?:\-.+)?\.js',
                  'common-' + hash + '.js'
                );
                replaceInFile(
                  path.join(module.exports.output.path, 'templates/src/common/head.hbs'),
                  'common(?:\-.+)?\.css',
                  'common-' + hash + '.css'
                );
            });
        }
    ],

    devtool: '#sourcemap',

    resolveLoader: {
        modules: ["node_modules"]
    },

    resolve: {
        extensions: ['.js', '.json', '.hbs', '.jpg', '.png', '.less', '.css'],
        modules: [path.resolve(__dirname, "src"), "node_modules"]
    }
};
