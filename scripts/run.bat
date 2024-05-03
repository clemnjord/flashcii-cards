@ECHO OFF

SET "ROOT=%~dp0\.."
SET "FLASHCII_CARDS_PATH=%ROOT%/example/rendered_asciidoc"

pushd "%ROOT%\backend"
start go run main.go
popd



SET "BACK_ADDRESS=127.0.0.1"
SET "BACK_PORT=8080"

pushd "%ROOT%\frontend"
start npm run dev
popd