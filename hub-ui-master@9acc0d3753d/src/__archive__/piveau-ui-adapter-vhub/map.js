/**
 * @author Dennis ritter
 * @created 25.10.17
 * @description
 */

export default class Map {
  constructor(baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * @description Service that creates an Open Streets Map using Leaflet and stores it in the response object of the returned promise.
   * @param Leaflet {Object} - The Leaflet dependency to create and manipulate maps (Open Street Map)
   * @param containerId {String} - The Template container element the map will be rendered in.
   * @returns {Promise}
   */
  static get(Leaflet, containerId) {
    return new Promise((resolve) => {
      // Init Map
      const map = Leaflet.map(containerId).setView([52.526, 13.314], 10);
      // Get Tiles
      Leaflet.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
        id: 'mapbox.streets',
        accessToken: 'pk.eyJ1IjoiZmFiaWFwZmVsa2VybiIsImEiOiJja2x3MzlvZ3UwNG85MnBseXJ6aGI2MHdkIn0.bFs2g4bPMYULlvDSVsetJg',
      }).addTo(map);
      const resData = {
        data: {
          map,
        },
      };
      resolve(resData);
    });
  }
}
