#!/bin/bash
set -e

PUID=${PUID:-1000}
PGID=${PGID:-1000}

# Create group/user if missing
if ! getent group "$PGID" >/dev/null; then
  groupadd -g "$PGID" appgroup
fi
if ! id -u "$PUID" >/dev/null 2>&1; then
  useradd -u "$PUID" -g "$PGID" -M -N -s /sbin/nologin appuser
fi

mkdir -p /app/data

chown -R "$PUID:$PGID" /app 2>/dev/null || true
chown -R "$PUID:$PGID" /app/data 2>/dev/null || true

echo "Starting with PUID=$PUID PGID=$PGID"
exec gosu "$PUID:$PGID" "$@"