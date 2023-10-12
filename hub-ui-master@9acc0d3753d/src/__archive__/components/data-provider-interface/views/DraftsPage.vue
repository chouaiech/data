<template>
  <div class="d-flex flex-column bg-transparent container-fluid justify-content-between mt-n5 content">
    <h1 class="small-headline">My draft datasets</h1>
    <ul class="list-group col-md-6 m-auto">
      <li
        v-for="{id, catalog} in getUserDrafts"
        :key="`draft@${id}`"
        class="list-group-item"
        :data-cy="`draft@${id}`"
      >
        <span>{{ id }}</span>
        <span class="float-right">
          <button type="button" class="btn btn-secondary">
            <app-link
              class="dropdown-toggle text-nowrap text-decoration-none"
              fragment="#" role="button" id="linkedDataDropdown"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false">
              <span :title="$t('message.metadata.linkedData')"
                    data-toggle="tooltip"
                    data-placement="top">
                     {{ $t('message.metadata.linkedData') }}
              </span>
            </app-link>
            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="linkedDataDropdown">
              <app-link :to="createLinkedMetricsURL(id, catalog, 'rdf')" target="_blank" class="dropdown-item"><div class="px-2 py-1">RDF/XML</div></app-link>
              <app-link :to="createLinkedMetricsURL(id, catalog, 'ttl')" target="_blank" class="dropdown-item"><div class="px-2 py-1">Turtle</div></app-link>
              <app-link :to="createLinkedMetricsURL(id, catalog, 'n3')" target="_blank" class="dropdown-item"><div class="px-2 py-1">Notation3</div></app-link>
              <app-link :to="createLinkedMetricsURL(id, catalog, 'nt')" target="_blank" class="dropdown-item"><div class="px-2 py-1">N-Triples</div></app-link>
              <app-link :to="createLinkedMetricsURL(id, catalog, 'jsonld')" target="_blank" class="dropdown-item"><div class="px-2 py-1">JSON-LD</div></app-link>
            </div>
          </button>
          <button type="button" class="btn btn-secondary" @click="handleEdit(id, catalog)">Edit</button>
          <button type="button" class="btn btn-primary" @click="handleConfirmPublish(id, catalog)">Publish</button>
          <button type="button" class="btn btn-danger" @click="handleConfirmDelete(id, catalog)">Delete</button>
        </span>
      </li>
    </ul>

    <app-confirmation-dialog id="draftsModal" confirm="Confirm" :loading="modalProps.loading" @confirm="modalProps.confirm">
      {{ modalProps.message }}
    </app-confirmation-dialog>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import $ from 'jquery';
import { AppLink } from "@piveau/piveau-hub-ui-modules";

export default {
  props: [],
  components: {
    appLink: AppLink,
  },
  data() {
    return {
      values: {},
      modalProps: {
        loading: false,
        message: 'Are you sure you want to delete this draft?',
        confirm: () => null,
      },
    };
  },
  computed: {
    ...mapGetters('auth', [
      'getUserDrafts',
    ]),
  },
  methods: {
    ...mapActions('auth', [
      'setIsDraft',
      'updateUserDrafts',
    ]),
    ...mapActions('snackbar', [
      'showSnackbar',
    ]),
    createLinkedMetricsURL(id, catalog, format) {
      return {
        path: `/dpi/draft/${id}.${format}`,
        query: {
          useNormalizedId: true,
          locale: this.$route.query.locale,
          catalogue: catalog,
        },
      };
    },
    handleEdit(id, catalog) {
      this.setIsDraft(true);
      this.$router.push({ name: 'DataProviderInterface-Edit', params: { catalog, property: 'datasets', id }, query: { locale: this.$route.query.locale }}).catch(() => {});
    },
    async handleDelete(id, catalog) {
      await this.doRequest('auth/deleteUserDraftById', { id, catalog });
      $('#draftsModal').modal('hide');
      this.showSnackbar({
        message: 'Draft successfully deleted',
        variant: 'success',
      });
    },
    async handlePublish(id, catalog) {
      await this.doRequest('auth/publishUserDraftById', { id, catalog });
      $('#draftsModal').modal('hide');
      this.showSnackbar({
        message: 'Draft successfully published',
        variant: 'success',
      });
      this.$router.push({ name: 'DatasetDetailsDataset', params: { ds_id: id }, query: { locale: this.$route.query.locale }}).catch(() => {});
    },
    handleConfirmPublish(id, catalog) {
      this.$set(this.modalProps, 'message', 'Are you sure you want to publish this draft?');
      this.$set(this.modalProps, 'confirm', () => this.handlePublish(id, catalog));
      $('#draftsModal').modal('show');
    },
    handleConfirmDelete(id, catalog) {
      this.$set(this.modalProps, 'message', 'Are you sure you want to delete this draft?');
      this.$set(this.modalProps, 'confirm', () => this.handleDelete(id, catalog));
      $('#draftsModal').modal('show');
    },
    async doRequest(action, { id, catalog }) {
      this.$Progress.start();
      this.$set(this.modalProps, 'loading', true);
      try {
        await this.$store.dispatch(action, { id, catalog });
        this.$Progress.finish();
      } catch (ex) {
        // Show snackbar
        this.showSnackbar({
          message: ex.message,
          color: 'error',
        });
        this.$Progress.fail();
      } finally {
        await new Promise(resolve => setTimeout(resolve, 500));
        this.$set(this.modalProps, 'loading', false);
      }
    },
  },
  created() {
    this.updateUserDrafts();
  },
};
</script>

<style lang="scss" scoped>
.nav-link {
  text-decoration: underline;
}

.active {
  text-decoration: none;
  font-weight: 700;
}

.dropdown-item {
  &:active {
    background-color: #868e96;
  }
}

#linkedDataDropdown {
  color: #FFFFFF;
}
</style>
