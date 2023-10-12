FROM alpine:3.18

WORKDIR /usr/src/app

COPY datadiscoverycentralservice ./

RUN apk --no-cache add gcompat

CMD ["/usr/src/app/datadiscoverycentralservice"]