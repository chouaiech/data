module.exports = {
  root: true,
  env: {
    node: true
  },
  'extends': [
    'plugin:vue/essential',
    'eslint:recommended'
  ],
  parserOptions: {
    parser: '@babel/eslint-parser'
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'vue/multi-word-component-names': 'off',
    'vue/no-mutating-props': 'off',
   // 'vue/no-use-v-if-with-v-for': 'off',
    // 'vue/no-unused-components': 'off',
  //  'vue/require-valid-default-prop': 'off',
   // 'vue/require-v-for-key': 'off',
    //'vue/no-duplicate-attributes': 'off',
 //   'no-constant-condition': 'off',
    //'no-unused-vars': 'off',
   // 'vue/no-side-effects-in-computed-properties': 'off'
  }
}
