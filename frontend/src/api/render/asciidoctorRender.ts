import Asciidoctor from 'asciidoctor';

const renderAsciidoc = (asciidocContent: string, cardPath: string): string => {
    const asciidoctor = Asciidoctor();

    const options = {
        attributes: {
            'card-url': cardPath,
        }
    };

    const html = asciidoctor.convert(asciidocContent, options);
    return html.toString()
};

export { renderAsciidoc };