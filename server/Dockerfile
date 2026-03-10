# Multi-stage build: compile TypeScript in a builder stage, then copy only the
# compiled output + production dependencies into the final image.

# builder stage ---------------------------------------------------------------
FROM node:22-alpine AS builder

WORKDIR /workspace

# copy package files and install all deps (including dev) so we can build
COPY package*.json ./
COPY tsconfig.json ./

# source files
COPY src ./src
# copy any other files needed for build (e.g. .env examples, typings)

RUN npm ci
RUN npm run build

# final image ----------------------------------------------------------------
FROM node:22-alpine

# use non-root user if desired (optional)

WORKDIR /app

# bring in the built code and production node_modules
COPY --from=builder /workspace/lib ./lib
COPY --from=builder /workspace/node_modules ./node_modules
# copy static assets served by express
COPY public ./public

ENV NODE_ENV=production

EXPOSE 8080
CMD ["node", "lib/index.js"]
