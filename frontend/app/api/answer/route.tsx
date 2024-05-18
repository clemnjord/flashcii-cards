import {NextResponse} from 'next/server'


export async function GET(request: Request) {
    try {
        const { searchParams } = new URL(request.url)
        const difficulty = searchParams.get("difficulty")

        void await fetch(`http://${process.env.BACKEND_ADDRESS}:${process.env.BACKEND_PORT}/api/answer`, {
            method: 'POST', // or 'PUT'
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ difficulty }),
            cache: 'no-store'
        });

        return NextResponse.json({ status: 200 })
    } catch (error) {
        return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 })
    }
}
