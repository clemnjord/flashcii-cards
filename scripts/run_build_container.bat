SET "SCRIPT_ROOT=%~dp0"

pushd %SCRIPT_ROOT%..
docker compose down
docker compose up -d --build
popd