package mid

import (
	"log/slog"
	"net/http"
)

type Logger struct {
	Log *slog.Logger
}

func (l Logger) Middleware(handler http.Handler) http.Handler {
	f := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		l.Log.Info("request started",
			slog.String("method", r.Method),
			slog.String("path", r.URL.Path),
		)

		handler.ServeHTTP(w, r)

		l.Log.Info("request completed",
			slog.String("method", r.Method),
		)
	})

	return f
}
