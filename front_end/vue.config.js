module.exports = {
  pluginOptions: {
    electronBuilder: {
      nodeIntegration: true,
      builderOptions: {
        icon:'src/assets/smenoteIcon.ico'      
      }
    }
  },
  chainWebpack: config => {
    config.module.rules.delete('eslint');
}
}
