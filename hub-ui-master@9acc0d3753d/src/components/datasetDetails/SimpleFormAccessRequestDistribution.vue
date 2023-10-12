
<template>
    <div>
        <div v-if="successMessage" class="success-popup">{{ successMessage }}</div>
        <div class="modal fade" id="accessRequestForm" tabindex="-1" role="dialog" aria-labelledby="simpleModalLabel"
            aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="modal-title">Access Request Form</h3>
                        <button class="close" type="button" data-dismiss="modal" aria-label="Close"><span>x</span></button>
                    </div>
                    <div class="modal-body">
                        <div>
                            <label for="firstName">First Name</label>
                            <input class="field-style" name="firstName" id="firstName" type="text" placeholder="First Name"
                                maxlength="200" v-model="details.firstName" @blur="validateFirstName"
                                ref="firstNameInput" />
                            <span class="error-message" v-if="firstNameError">{{ firstNameError }}</span>
                        </div>
                        <div>
                            <label for="lastName">Last Name</label>
                            <input class="field-style" name="lastName" id="lastName" type="text" placeholder="Last Name"
                                maxlength="200" v-model="details.lastName" @blur="validateLastName" ref="lastNameInput" />
                            <span class="error-message" v-if="lastNameError">{{ lastNameError }}</span>

                        </div>
                        <div>
                            <label for="email">Email Address</label>
                            <input class="field-style" name="email" id="email" type="email" placeholder="Email Address"
                                v-model="details.email" @blur="validateEmail" ref="emailInput" />
                            <span class="error-message" v-if="emailError">{{ emailError }}</span>

                        </div>
                        <div>
                            <label for="distributions">Dataset/distribution(s)</label>

                            <multiselect v-model="details.distributions" :multiple="true" :close-on-select="false"
                                :clear-on-select="false" placeholder="Pick some" label="label" track-by="id"
                                :options="distributionOptions" :readonly="isMultiselectReadonly" :class="multiselectClass">
                            </multiselect>
                            <span class="error-message" v-if="distributionError">{{ distributionError }}</span>


                        </div>
                        <div>
                            <label for="reason">Reason</label>

                            <textarea class="field-style" name="reason" id="reason" placeholder="Reason"
                                v-model="details.reason" maxlength="2000" @blur="validateReason"
                                ref="reasonInput"></textarea>
                            <span v-if="reasonError" class="error-message">{{ reasonError }}</span>

                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" v-on:click="sendForm">Send</button>
                        <button class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    </div>

                </div>
            </div>

        </div>

    </div>
</template>

<script>
import $ from "jquery";
import axios from 'axios';
import Multiselect from 'vue-multiselect';
import { mapGetters } from "vuex";
import {
    helpers
} from "@piveau/piveau-hub-ui-modules";
const { getTranslationFor } = helpers;
export default {
    components: {
        Multiselect
    },

    computed: {
        ...mapGetters('datasetDetails', [
            'getLanguages'
        ]),
        /* distributionOptions() {
            return this.details.distributions.map(dist => {
                return {
                    ...dist,
                    label: this.getDistributionTitle(dist)
                };
            });
        }, */
        distributionOptions() {
            return this.allDistributions.map(dist => {
                return {
                    ...dist,
                    label: this.getDistributionTitle(dist)
                };
            });
        },

        isMultiselectReadonly() {
            return this.isModalShown && this.allDistributions.length === 1;
        },
        multiselectClass() {
            return {
                'readonly-multiselect': this.isMultiselectReadonly
            };
        }
    },
    props: {
        initialDetails: {
            type: Object,
            default: () => ({ firstName: '', lastName: '', email: '', distributions: '', reason: '', })
        },
        allDistributions: {
            type: Array,
            default: () => []
        }
    },
    data() {
        return {
            details: { ...this.initialDetails, distributions: [] },
            emailError: null,
            reasonError: null,
            firstNameError: null,
            lastNameError: null,
            distributionError: null,
            successMessage: null,
            isModalShown: false,




        };
    },
    mounted() {
        this.$root.$on('show-modal-simple', details => {
            this.details = { ...details };

            this.allDistributions = details.distributions.map(dist => {
                return {
                    ...dist,
                    label: this.getDistributionTitle(dist)
                };
            });
            if (this.allDistributions.length === 1) {
                this.details.distributions = [this.allDistributions[0]];
            } else {
                this.details.distributions = [];
            }

            this.isModalShown = true;

            $("#accessRequestForm").modal("show");
        });
        $('#accessRequestForm').on('hidden.bs.modal', () => {
            this.isModalShown = false;
        });
    },
    methods: {
        getDistributionTitle(distribution) {

            return distribution.title ? getTranslationFor(distribution.title, this.$route.query.locale, this.getLanguages) : '-';
        },
        getTranslationFor,
        async sendForm() {
            //alert(`firstName: ${this.details.firstName}, lastName: ${this.details.lastName}`);
            this.validateFirstName();
            this.validateLastName();
            this.validateEmail();
            this.validateDistributions();
            this.validateReason();


            if (this.firstNameError) {
                this.$refs.firstNameInput.focus();
                return;
            }
            if (this.lastNameError) {
                this.$refs.lastNameInput.focus();
                return;
            }

            if (this.emailError) {
                this.$refs.emailInput.focus();
                return;  // Exit early if there's an email error
            }
            if (this.distributionError) {
                return;  // Exit early if there's a distribution error
            }
            if (this.reasonError) {
                this.$refs.reasonInput.focus();
                return;  // Exit early if there's an email error
            }
            const formDetails = {
                "First Name": this.details.firstName,
                "Last Name": this.details.lastName,
                "Email Address": this.details.email,
                "Datasets": this.details.distributions ? this.details.distributions.map(dist => dist.id).join(', ') : '',
                "Reason": this.details.reason
            };

            const data = {
                application_id: "abcd",
                catalog_id: "ecdc",
                form: formDetails
            };

            try {

                const response = await axios.post('http://localhost:8103/datapermit/permit', data);
                console.log('Response:', response);

                $("#accessRequestForm").modal("hide");

                this.successMessage = 'Access Request sent successfully!';

                setTimeout(() => {
                    this.successMessage = null;
                }, 5000);

            } catch (error) {
                console.error('Error:', error);

            }
        },
        validateReason() {
            const alphanumericRegex = /^[a-z0-9\s]+$/i;
            if (!this.details.reason || !alphanumericRegex.test(this.details.reason)) {
                this.reasonError = 'Please enter an alphanumeric reason.';
            } else {
                this.reasonError = null;
            }
        },
        validateEmail() {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!this.details.email) {
                this.emailError = 'Email is required.';
            } else if (!emailRegex.test(this.details.email)) {
                this.emailError = 'Invalid email format.';
            } else {
                this.emailError = '';
            }
        },
        validateFirstName() {
            if (!this.details.firstName || !this.details.firstName.trim()) {
                this.firstNameError = 'First Name is required.';
            } else {
                this.firstNameError = '';
            }
        },
        validateLastName() {
            if (!this.details.lastName || !this.details.lastName.trim()) {
                this.lastNameError = 'Last Name is required.';
            } else {
                this.lastNameError = '';
            }
        },
        validateDistributions() {
            if (!this.details.distributions.length) {
                this.distributionError = 'At least one distribution must be selected.';
            } else {
                this.distributionError = null;
            }
        },
    }
}
</script>

<style scoped>
.multiselect--readonly .multiselect__select {
    display: none;
}

.multiselect--readonly .multiselect__tags {
    pointer-events: none;
}

.readonly-multiselect {
    pointer-events: none;
}

.modal-dialog {
    max-width: 600px !important;
}

.multiselect__tag {
    font-size: 18px !important;
}

.success-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: green;
    color: white;
    padding: 20px;
    border-radius: 5px;
    z-index: 1000;
}

.error-message {
    color: red;

    margin-top: 4px;
    display: block;
}

.field-style {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #c0c0c0;
    border-radius: 4px;
    box-sizing: border-box;
}

.field-style:focus {
    outline: solid #c0c0c0;
}

label {
    margin-bottom: 0px;
}
</style>