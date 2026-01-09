FROM debian:trixie-slim

ENV SDKMAN_JDK_VERSION=21.0.8-tem
ENV SDKMAN_GRADLE_VERSION=8.14.3
ENV SDKMAN_QUARKUS_VERSION=3.26.1
ENV NODE_MAJOR_VERSION=24
ENV PNPM_VERSION=10.11.0

# -------------------------------------------------------
# Base dependencies
# -------------------------------------------------------
RUN apt-get update && apt-get install -y \
    git curl wget unzip zip \
    gcc zlib1g-dev \
    ca-certificates \
    libxtst6 graphviz libxi6 \
    && rm -rf /var/lib/apt/lists/*

# -------------------------------------------------------
# Install SDKMAN
# -------------------------------------------------------
RUN curl -s "https://get.sdkman.io" | bash

# Install Java, Gradle, Quarkus
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java $SDKMAN_JDK_VERSION"
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle $SDKMAN_GRADLE_VERSION"
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install quarkus $SDKMAN_QUARKUS_VERSION"

# -------------------------------------------------------
# Install Node.js
# -------------------------------------------------------
RUN curl -fsSL https://deb.nodesource.com/setup_${NODE_MAJOR_VERSION}.x | bash - \
    && apt-get update \
    && apt-get install -y nodejs \
    && rm -rf /var/lib/apt/lists/*

# -------------------------------------------------------
# Install PNPM via Corepack (now that Node is installed)
# -------------------------------------------------------
RUN corepack enable && corepack prepare pnpm@${PNPM_VERSION} --activate

# -------------------------------------------------------
# Install Docker CLI
# -------------------------------------------------------
RUN install -m 0755 -d /etc/apt/keyrings \
    && curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc \
    && chmod a+r /etc/apt/keyrings/docker.asc \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian $(. /etc/os-release && echo "$VERSION_CODENAME") stable" \
        > /etc/apt/sources.list.d/docker.list \
    && apt-get update \
    && apt-get install -y docker-ce-cli \
    && rm -rf /var/lib/apt/lists/*
