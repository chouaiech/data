# piveau-cookie-consent

A configurable Vue component that displays a cookie banner.

### Installation

```bash
npm install @piveau/piveau-cookie-consent
```

### Usage Example

```html
<!-- css import for when you want to import the component css into your css file/files  -->
@import '/path/to/node_modules/@piveau/piveau-cookie-consent.css';

<!-- css import for when you're importing the css directly in your js  -->
import '@piveau/piveau-cookie-consent/dist/piveau-cookie-consent.css'

import PiveauCookieConsent from '@piveau/piveau-cookie-consent'
Vue.component('pv-cookie-consent', PiveauCookieConsent)
```

```html
<pv-cookie-consent
    :ref="'myPanel1'"
    :elementId="'myPanel1'"
    :debug="false"
    :position="'bottom-left'"
    :type="'floating'"
    :disableDecline="false"
    :transitionName="'slideFromBottom'"
    :showPostponeButton="false"
    @status="cookieStatus"
    @clicked-accept="cookieClickedAccept"
    @clicked-decline="cookieClickedDecline">

    <!-- Optional -->
    <div slot="postponeContent">
        &times;
    </div>

    <!-- Optional -->
    <div slot="message">
        We use cookies to ensure you get the best experience on our website. <a href="https://cookiesandyou.com/" target="_blank">Learn More...</a>
    </div>

    <!-- Optional -->
    <div slot="declineContent">
       OPT OUT
    </div>

    <!-- Optional -->
    <div slot="acceptContent">
        GOT IT!
    </div>
</vue-cookie-accept-decline>
```

### Props

| prop           | type    | required | default         | possible values                     | description                                                          |
|----------------|---------|----------|-----------------|-------------------------------------|----------------------------------------------------------------------|
| ref            | String  | no       | none            | Any String                          | Unique string that gives you control over the component |
| elementId      | string  | yes      | none            | Any String                          | The unique id for the instance. This string will be appened to the string 'vue-cookie-accept-decline-' to allow for multiple components. |
| debug          | boolean | no       | false           | true, false                         | If true, the cookie is never saved, only the events will be emitted |
| position       | string  | no       | bottom          | For floating: bottom-left, bottom-right, top-left, top-right -- For bar: bottom, top | Position of the banner   |
| type           | string  | no       | floating        | floating, bar                       | Type of banner   |
| disableDecline | boolean | no       | false           | true, false                         | If true, the 'opt out' button is not shown |
| transitionName | string  | no       | slideFromBottom | slideFromBottom, slideFromTop, fade | Banner animation type    |
| showPostponeButton | boolean  | no  | false           | true, false                         | Optionally show a close button that allows the user to postpone selecting an option. |
| cookie | string | no | piveau-cookie-accept-decline-state | Any String | Name of cookie
cookieExpires | string | no | 100D | Any String formatted as time unit | Specify the expiration date of given cookies
noCookiesUntilConsent | boolean | no | false | true, false | If true, provides no cookies until given consent


### Events

| event          | value                     | description                                                   |
|----------------|---------------------------|---------------------------------------------------------------|
| status         | 'accept', 'decline', 'postpone', null | Event will be emitted when component is created.             |
| clicked-accept  | none                     | Event will be emitted when accept is clicked on the banner.   |
| clicked-decline | none                     | Event will be emitted when declined is clicked on the banner. |
| clicked-postpone | none                    | Event will be emitted when postponed is clicked on the banner. |
| removed-cookie | none                      | Event will be emitted when the cookie has been removed using the `removeCookie()` method. |

### Slots

There are slots for your own custom `message`, `declineContent`, `acceptContent`, this is good for providing your own link or whatever HTML content you want in your message/buttons - like icons.

| name           | default value |
|----------------|--------------|
| message        | We use cookies to ensure you get the best experience on our website. <a href="https://cookiesandyou.com/" target="_blank">Learn More...</a> |
| declineContent | Opt Out |
| acceptContent  | Got It! |
| postponeContent  | `&times;` |

### Methods

Note - call these methods through the `ref` you set up with your component. Example: `this.$refs.myPanel1.removeCookie()`.

| method    | parameters  | description                    |
|---------|-------|--------------------------------|
| removeCookie | none | Used to delete the unique cookie for the instance you are acting on. |
| init | none | Evaluates the cookie status and shows the panel if proper conditions are met. Useful for re-showing the panel after someone uses the `removeCookie` method. |

### SASS Structure

```sass
.cookie {
    // Bar style
    &__bar {
        &--bottom {
        }

        &--top {
        }

        &__postpone-button {
        }

        &__content {
        }

        &__buttons {

            &__button {
                &--accept {
                }

                &--decline {
                }
            }
        }
    }

    // Floating style
    &__floating {
        &--bottom-left {
        }

        &--bottom-right {
        }

        &--top-right {
        }

        &--top-left {
        }

        &__postpone-button {
        }

        &__content {
        }

        &__buttons {

            &__button {
                &--accept {
                }

                &--decline {
                }
            }
        }
    }
}
```

## Developing

You can see changes to the source code locally by running the demo. For testing this plugin as an npm package on another project, build and install this plugin like this:
```
npm run build:lib
cd ../{OTHER_PROJECT_FOLDER}
npm install ../{THIS_PROJECT_ROOT}
```

Please refer to [the contributing guide](./CONTRIBUTING.md) before submitting changes to the code.
## Project setup
```
npm install
```

### Run unit tests
```
npm run test:unit
```

### Compiles and minifies for production
```
npm run build:lib
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
