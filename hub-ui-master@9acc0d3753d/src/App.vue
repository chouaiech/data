<template>
	<div class="app">
		<div class="app-snackbar position-fixed fixed-bottom m-3 m-md-5 py-5 d-flex justify-content-center w-100">
			<app-snackbar />
		</div>
		<vue-progress-bar />
		<div class="site-wrapper">
			<deu-header project="hub" active-menu-item="data" enable-authentication use-breadcrumbs
				use-breadcrumbs-route-meta @login="euLogin" @logout="euLogout" :showSparql="showSparql" />
			<router-view class="content" :key="`${$route.fullPath}`" />

			<deu-footer :authenticated="keycloak && keycloak.authenticated"
				:use-login="$env.navigation.bottom.login.useLogin" :login="$env.navigation.bottom.login.loginURL"
				:login-title="$env.navigation.bottom.login.loginTitle" :logout="$env.navigation.bottom.login.logoutURL"
				:logout-title="$env.navigation.bottom.login.logoutTitle" @click-follow-link="handleFollowClick" />

		</div>
		<dpiMenu v-if="keycloak && keycloak.authenticated"></dpiMenu>
		<cookie-consent :piwik-instance="$piwik" />
	</div>
</template>

<script>
/* eslint-disable no-underscore-dangle */
/*import CookieConsent from '@deu/deu-cookie-consent';
import '@deu/deu-cookie-consent/dist/deu-cookie-consent.css';*/
import { DpiMenu } from '@piveau/piveau-hub-ui-modules';

export default {
	name: 'app',
	components: {
		//CookieConsent,
		DpiMenu,
	},
	metaInfo() {
		return {
			titleTemplate(chunk) {
				return chunk ? `${chunk} - Health.data.eu` : 'Health.data.eu';
			},
			meta: [
				{ name: 'description', vmid: 'description', content: 'data.europa.eu' },
				{ name: 'keywords', vmid: 'keywords', content: this.$env.keywords },
			],
			htmlAttrs: {
				lang: this.$route.query.locale,
			}
		};
	},
	data() {
		return {
			tracker: null,
			matomoURL: this.$env.tracker.trackerUrl,
			piwikId: this.$env.tracker.siteId,
			lastRoute: null,
			keycloak: this.$keycloak,
			showSparql: this.$env.navigation.top.main.data.sparql.show
		};
	},
	computed: {},
	methods: {
		euLogin() {
			window.location.href = "https://data.europa.eu/euodp/data/user/login";
		},
		euLogout() { },
		handleFollowClick(url) {
			this.$piwik.trackOutlink(url);
		},
	},
};

// JavaScript code to hide the "Services" menu item
document.addEventListener('DOMContentLoaded', function () {
	document.querySelector('#menu-item-list').childNodes[2].textContent = ""
	document.querySelector('#menu-item-list').childNodes[3].textContent = ""
	document.querySelector('#menu-item-list').childNodes[4].textContent = ""
	document.querySelector('#menu-item-list').childNodes[5].textContent = ""
	
	document.querySelector('.deu-headline').textContent	= "health.data.eu - The official portal for European Health data"
});

</script>

<style lang="scss">
// Normalizes default css rules. See: https://github.com/necolas/normalize.css
@import './styles/utils/normalize.css';

@font-face {
	font-family: "Ubuntu";
	src: local("Ubuntu"), url(../static/fonts/Ubuntu/Ubuntu-Regular.ttf) format("truetype");
}

* {
	box-sizing: border-box;
}

.site-wrapper header {
	display: initial;
}

.container-fluid {
	max-width: 1340px !important;
}
</style>

<style lang="scss" scoped>
.app {
	background-color: #fff;
}

.site-wrapper {
	border: 0;
	max-width: none;
	box-shadow: none;
	display: initial;

	.content {
		padding: 30px 30px 0 30px;
		margin-top: 15px;
		margin-bottom: 15px;
		background-color: white;
	}
}

.app-snackbar {
	z-index: 9999;
	pointer-events: none;
}
</style>
