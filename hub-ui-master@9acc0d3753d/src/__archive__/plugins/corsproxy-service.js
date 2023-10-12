/**
 * Vue plugin that exposes a http client based on Axios,
 * which proxies client requests to a given CORS proxy service
 */

import axios from 'axios';

const CorsproxyService = {
  install(Vue, corsproxyUrl = '') {
    // Create a modified axios instance such that its API
    // works just as if requests are called without proxy
    const corsproxyService = axios.create();

    // If corsproxyUrl is not given, just provide unmodified axios client
    // to pass requests as-is
    if (corsproxyUrl) {
      const hasTrailingSlash = corsproxyUrl.substr(-1) === '/' || !corsproxyUrl;
      const normalizedCorsproxyUrl = hasTrailingSlash
        ? corsproxyUrl
        : `${corsproxyUrl}/`;

      corsproxyService.interceptors.request.use((config) => {
        const requestedUri = axios.getUri({
          url: config.url,
          params: {
            ...config.params,
          },
        });

        const modifiedConfig = { ...config };
        modifiedConfig.url = normalizedCorsproxyUrl;
        modifiedConfig.params = {
          uri: requestedUri,
        };
        return modifiedConfig;
      });
    }

    Vue.prototype.$corsproxyService = corsproxyService; // eslint-disable-line
  },
};

export default CorsproxyService;
