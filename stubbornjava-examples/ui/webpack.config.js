var ExtractTextPlugin = require('extract-text-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin');

var webpack = require('webpack');
var path = require('path');

module.exports = {
    // The standard entry point and output config
    entry: {
        app: './src/app'
        // Use extra files to induclde dependencies only used on some pages
        // A good exmaple would be a graphing library.
        //extras: './src/extras'
    },
    output: {
        path: __dirname + '/assets/static',
        filename: 'js/[name].js',
        chunkFilename: '[id].js',
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
                //exclude: /(node_modules|bower_components)\/[^s]/, //^shared
                loader:  'babel-loader',
                query:   {
                    presets: ['es2015']
                }
            }
        ]
    },

    // Use the plugin to specify the resulting filename (and add needed behavior to the compiler)
    plugins: [
        new ExtractTextPlugin('css/[name].css'),
        new webpack.optimize.UglifyJsPlugin({minimize: true}),
        new OptimizeCssAssetsPlugin(),
        new CopyWebpackPlugin([
            {
                from: 'src/**/*.hbs',
                to: './templates/'
            }
        ]),
        new webpack.ProvidePlugin({
          // We shouldn't need all of these but ran into some issues
          // Adding all of them fixed it :(
          $: "jquery",
          jQuery: "jquery",
          'window.$': 'jquery',
          'window.jQuery': 'jquery'
       })
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
