import {NextResponse} from 'next/server'
import {parse} from "node-html-parser";

export async function GET() {

    const extractHtml = (content: string, classId: string) => {
        const root = parse(content);
        const questionDiv = root.querySelector(classId);
        if (questionDiv) {
            return questionDiv.outerHTML;
        }
        return "";
    };

    try {
        const response = await fetch(`http://${process.env.BACKEND_ADDRESS}:${process.env.BACKEND_PORT}/api/nextQuestion`, { cache: 'no-store'});
        const asciipage = await response.json();

        const pageId = asciipage.id;
        const question = extractHtml(asciipage.data, "#question");
        const answer = extractHtml(asciipage.data, "#answer");

        return NextResponse.json({ pageId, question, answer });
    } catch (error) {
        return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 })
    }
}
