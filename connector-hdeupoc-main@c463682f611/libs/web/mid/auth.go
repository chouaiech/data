package mid

import (
	"net/http"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/web/apikey"
)

type Auth struct {
	Keys map[string]string
}

func (a Auth) Middleware(handler http.Handler) http.Handler {
	f := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {

		key := r.Header.Get("authorization")
		err := apikey.Authenticate(key, a.Keys)
		if err != nil {
			http.Error(w, err.Error(), http.StatusUnauthorized)
			return
		}

		handler.ServeHTTP(w, r)
	})

	return f
}
